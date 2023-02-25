package org.nakedobjects.metamodel.facets.propparam.validate.mask;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class MaskEvaluator {

    interface Converter {
        void convert(String str, StringBuilder buf);
    }

    static class RegExConverter implements Converter {
        private final String mask;
        private final String regex;

        public RegExConverter(final String mask, final String regex) {
            this.mask = mask;
            this.regex = regex;
        }

        public String getMask() {
            return mask;
        }

        public String getRegex() {
            return regex;
        }

        public void convert(final String str, final StringBuilder buf) {
            final String convert = str.replace(mask, regex);
            if (!convert.equals(str)) {
                buf.append(convert);
            }
        }
    }

    @SuppressWarnings("serial")
    private static List<Converter> converters = new ArrayList<Converter>() {
        {
            add("#", "[0-9]");
            // add(".", "[\\" + DecimalFormatSymbols.getInstance().getDecimalSeparator()+"]");
            // add(",", "["+DecimalFormatSymbols.getInstance().getGroupingSeparator()+"]");
            add("&", "[A-Za-z]");
            add("?", "[A-Za-z]");
            add("A", "[A-Za-z0-9]");
            add("a", "[ A-Za-z0-9]");
            add("9", "[ 0-9]");
            add("U", "[A-Z]");
            add("L", "[a-z]");

            add(new Converter() {
                public void convert(final String str, final StringBuilder buf) {
                    if (buf.length() == 0) {
                        buf.append(str);
                    }
                }
            });
        }

        public void add(final String mask, final String regex) {
            add(new RegExConverter(mask, regex));
        }
    };

    private final Pattern pattern;

    public MaskEvaluator(final String mask) {
        final StringBuilder buf = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            final String charAt = "" + mask.charAt(i);
            for (final Converter converter : converters) {
                converter.convert(charAt, buf);
            }
        }
        pattern = Pattern.compile(buf.toString());
    }

    public boolean evaluate(final String str) {
        return pattern.matcher(str).matches();
    }

}
