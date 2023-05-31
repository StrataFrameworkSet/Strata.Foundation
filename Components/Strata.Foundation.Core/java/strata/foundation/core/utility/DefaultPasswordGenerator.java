//////////////////////////////////////////////////////////////////////////////
// DefaultPasswordGenerator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import com.google.common.primitives.Chars;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public
class DefaultPasswordGenerator
    implements IPasswordGenerator
{
    private final int itsLength;
    private final int itsMinAlpha;
    private final int itsMinNum;
    private final int itsMinSpecial;

    private static int ALPHA_TYPE = 0;
    private static int NUM_TYPE = 1;
    private static int SPECIAL_TYPE = 2;
    private static char[][] theirChars =
        {
            {
                'A','B','C','D','E','F','G','H','I','J','K','L','M',
                'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m',
                'n','o','q','r','s','t','u','v','w','x','y','z'
            },
            {'0','1','2','3','4','5','6','7','8','9'},
            {'~','!','#','$','%','&','*'}
        };

    public
    DefaultPasswordGenerator(
        int length,
        int minAlpha,
        int minNum,
        int minSpecial)
    {
        itsLength     = length;
        itsMinAlpha   = minAlpha;
        itsMinNum     = minNum;
        itsMinSpecial = minSpecial;

        if (minAlpha+minNum+minSpecial > length)
            throw new IllegalArgumentException(
                "length is insufficient for specified minimums");
    }

    @Override
    public String
    getNextPassword()
    {
        Random          random  = new SecureRandom();
        StringBuilder   builder = new StringBuilder();
        List<Character> used    = new ArrayList<>();

        for (int i=0;i<itsMinAlpha;i++)
            builder.append(getNextChar(ALPHA_TYPE,random,used));

        for (int i=0;i<itsMinNum;i++)
            builder.append(getNextChar(NUM_TYPE,random,used));

        for (int i=0;i<itsMinSpecial;i++)
            builder.append(getNextChar(SPECIAL_TYPE,random,used));

        while (builder.length() < itsLength)
        {
            int charType = random.nextInt(3);

            builder.append(getNextChar(charType,random,used));
        }

        return shuffle(builder.toString(),random);
    }

    private static char
    getNextChar(int type,Random random,List<Character> used)
    {
        int bound = theirChars[type].length;
        char output = theirChars[type][random.nextInt(bound)];
        int attempt = 0;

        while (attempt++ < 10 && used.contains(output))
            output = theirChars[type][random.nextInt(bound)];

        used.add(output);
        return output;
    }

    private static String
    shuffle(String input,Random random)
    {
        List<Character> characters =
            Chars.asList(input.toCharArray());
        StringBuilder output = new StringBuilder();

        Collections.shuffle(characters,random);

        characters
            .stream()
            .forEach(c -> output.append(c));

        return output.toString();
    }
}

//////////////////////////////////////////////////////////////////////////////
