package io.github.therealmone.core.utils;

import io.github.therealmone.core.collections.Collection;
import io.github.therealmone.core.exceptions.CasterException;

public class Caster {
    public int castToInt(final String parameter) {
        if(isNotInteger(parameter)) {
            throw new CasterException("Wrong type of: " + parameter);
        }

        return Double.valueOf(parameter).intValue();
    }

    public int castToInt(final Double parameter) {
        if(isNotInteger(parameter)) {
            throw new CasterException("Wrong type of: " + parameter);
        }

        return parameter.intValue();
    }

    public double castToDouble(final String parameter) {
        if(isNotDouble(parameter)) {
            throw new CasterException("Wrong type of: " + parameter);
        }

        return Double.parseDouble(parameter);
    }

    public double castToDouble(Object object) {
        if(object instanceof Double) {
            return (Double) object;
        } else if(object instanceof String) {
            return castToDouble((String) object);
        } else {
            throw new CasterException("Wrong type of: " + object);
        }
    }

    public boolean castToBoolean(final String parameter) {
        if(isNotBoolean(parameter)) {
            throw new CasterException("Wrong type of: " + parameter);
        }

        return Boolean.valueOf(parameter);
    }

    public boolean castToBoolean(final Object object) {
        if(object instanceof Boolean) {
            return (Boolean) object;
        } else if(object instanceof String) {
            return castToBoolean((String) object);
        } else {
            throw new CasterException("Wrong type of: " + object);
        }
    }

    public Object autoCastToObject(final String parameter) {
        if (!isNotDouble(parameter)) {
            return Double.valueOf(parameter);
        } else if(!isNotBoolean(parameter)) {
            return Boolean.valueOf(parameter);
        } else if(!isNotString(parameter)) {
            return parameter;
        } else {
            throw new CasterException("Unknown type of: " + parameter);
        }
    }

    public String autoCastToString(final Object object) {
        if(object instanceof Integer) {
            return ((Integer) object).toString();
        } else if(object instanceof Double) {
            return ((Double) object).toString();
        } else if(object instanceof Boolean) {
            return ((Boolean) object).toString();
        } else if(object instanceof String) {
            return (String) object;
        } else if (object instanceof Collection) {
            return "\"" + ((Collection) object).toString() + "\"";
        } else {
            throw new CasterException("Unknown type of: " + object);
        }
    }

    public boolean isNotDouble(final String parameter) {
        final String Digits     = "(\\p{Digit}+)";
        final String HexDigits  = "(\\p{XDigit}+)";
        // an exponent is 'e' or 'E' followed by an optionally
        // signed decimal integer.
        final String Exp        = "[eE][+-]?"+Digits;
        final String fpRegex    =
                ("[\\x00-\\x20]*"+ // Optional leading "whitespace"
                        "[+-]?(" +         // Optional sign character
                        "NaN|" +           // "NaN" string
                        "Infinity|" +      // "Infinity" string

                        // A decimal floating-point string representing a finite positive
                        // number without a leading sign has at most five basic pieces:
                        // Digits . Digits ExponentPart FloatTypeSuffix
                        //
                        // Since this method allows integer-only strings as input
                        // in addition to strings of floating-point literals, the
                        // two sub-patterns below are simplifications of the grammar
                        // productions from the Java Language Specification, 2nd
                        // edition, section 3.10.2.

                        // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                        "((("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+

                        // . Digits ExponentPart_opt FloatTypeSuffix_opt
                        "(\\.("+Digits+")("+Exp+")?)|"+

                        // Hexadecimal strings
                        "((" +
                        // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                        "(0[xX]" + HexDigits + "(\\.)?)|" +

                        // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                        "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

                        ")[pP][+-]?" + Digits + "))" +
                        "[fFdD]?))" +
                        "[\\x00-\\x20]*");// Optional trailing "whitespace"

        return !parameter.matches(fpRegex);
    }

    public boolean isNotInteger(final String parameter) {

        return !(!isNotDouble(parameter) && Double.parseDouble(parameter) % 1 == 0);
    }

    public boolean isNotInteger(final Double parameter) {
        return !(parameter % 1 == 0);
    }

    public boolean isNotBoolean(final String parameter) {
        return !(parameter.equals("true") || parameter.equals("false"));
    }

    public boolean isNotString(final String parameter) {
        return !(parameter.matches("^\"(.*?)\"$"));
    }

}
