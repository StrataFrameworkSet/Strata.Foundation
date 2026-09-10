//////////////////////////////////////////////////////////////////////////////
// DefaultSecurityCodeGenerator.java
//////////////////////////////////////////////////////////////////////////////

package strata.foundation.core.utility;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * Default {@link ISecurityCodeGenerator} implementation that generates
 * random security codes of a fixed length composed from alphabetic and
 * numeric characters, guaranteeing a configurable minimum number of each
 * character type.
 * </p><br/>
 * <b>Examples</b><br/>
 * <pre>
 * ISecurityCodeGenerator generator = new DefaultSecurityCodeGenerator(6,0,6);
 *
 * String code = generator.getNextSecurityCode();
 * </pre>
 */
public
class DefaultSecurityCodeGenerator
    implements ISecurityCodeGenerator
{
    private final int itsLength;
    private final int itsMinAlpha;
    private final int itsMinNum;

    private static int ALPHA_TYPE = 0;
    private static int NUM_TYPE = 1;
    private static char[][] theirChars =
        {
            {
                'A','B','C','D','E','F','G','H','I','J','K','L','M',
                'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                'a','b','c','d','e','f','g','h','i','j','k','l','m',
                'n','o','q','r','s','t','u','v','w','x','y','z'
            },
            {'0','1','2','3','4','5','6','7','8','9'}
        };

    public DefaultSecurityCodeGenerator(int length)
    {
        this(length,0,length);
    }

    public DefaultSecurityCodeGenerator(
        int length,
        int minAlpha,
        int minNum)
    {
        itsLength     = length;
        itsMinAlpha   = minAlpha;
        itsMinNum     = minNum;

        if (minAlpha+minNum > length)
            throw new IllegalArgumentException(
                "length is insufficient for specified minimums");
    }

    @Override
    public String
    getNextSecurityCode()
    {
        Random          random  = new SecureRandom();
        StringBuilder   builder = new StringBuilder();
        List<Character> used    = new ArrayList<>();

        for (int i=0;i<itsMinAlpha;i++)
            builder.append(getNextChar(ALPHA_TYPE,random,used));

        for (int i=0;i<itsMinNum;i++)
            builder.append(getNextChar(NUM_TYPE,random,used));

        while (builder.length() < itsLength)
        {
            int charType = random.nextInt(2);

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
            input
                .chars()
                .mapToObj(c -> (char)c)
                .collect(Collectors.toList());
        StringBuilder output = new StringBuilder();

        Collections.shuffle(characters,random);

        characters
            .stream()
            .forEach(c -> output.append(c));

        return output.toString();
    }
}

//////////////////////////////////////////////////////////////////////////////
