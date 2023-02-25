package org.nakedobjects.metamodel.value;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.TimeZone;

import org.nakedobjects.applib.adapters.EncodingException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.DateValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;


public abstract class ValueSemanticsProviderAbstractTemporal extends ValueSemanticsProviderAbstract implements DateValueFacet {

    protected static final String ISO_ENCODING_FORMAT = "iso_encoding";
    private static final TimeZone UTC_TIME_ZONE;

    static {
        TimeZone timeZone = TimeZone.getTimeZone("Etc/UTC");
        if (timeZone == null) {
            // for dotnet compatibility - Etc/UTC fails in dotnet
            timeZone = TimeZone.getTimeZone("UTC");
        }
        UTC_TIME_ZONE = timeZone;
    }

    /**
     * The facet type, used if not specified explicitly in the constructor.
     */
    public static Class<? extends Facet> type() {
        return DateValueFacet.class;
    }

    protected static DateFormat createDateFormat(final String mask) {
        return new SimpleDateFormat(mask);
    }

    private final DateFormat encodingFormat;
    protected DateFormat format;

    /**
     * Uses {@link #type()} as the facet type.
     */
    public ValueSemanticsProviderAbstractTemporal(
            final String propertyName,
            final FacetHolder holder,
            final Class<?> adaptedClass,
            final int typicalLength,
            final boolean immutable,
            final boolean equalByContent,
            final Object defaultValue,
            final NakedObjectConfiguration configuration,
            final SpecificationLoader specificationLoader,
            final RuntimeContext runtimeContext) {
        this(propertyName, type(), holder, adaptedClass, typicalLength, immutable, equalByContent, defaultValue, configuration,
                specificationLoader, runtimeContext);
    }

    /**
     * Allows the specific facet subclass to be specified (rather than use {@link #type()}.
     */
    public ValueSemanticsProviderAbstractTemporal(
            final String propertyName,
            final Class<? extends Facet> facetType,
            final FacetHolder holder,
            final Class<?> adaptedClass,
            final int typicalLength,
            final boolean immutable,
            final boolean equalByContent,
            final Object defaultValue,
            final NakedObjectConfiguration configuration,
            final SpecificationLoader specificationLoader,
            final RuntimeContext runtimeContext) {
        super(facetType, holder, adaptedClass, typicalLength, immutable, equalByContent, defaultValue, configuration,
                specificationLoader, runtimeContext);
        final Hashtable formats = formats();
        final Enumeration elements = formats.elements();
        while (elements.hasMoreElements()) {
            final DateFormat format = (DateFormat) elements.nextElement();
            format.setLenient(false);
            if (ignoreTimeZone()) {
                format.setTimeZone(UTC_TIME_ZONE);
            }
        }
        final String defaultFormat = configuration.getString(ConfigurationConstants.ROOT + "value.format." + propertyName,
                defaultFormat());
        final String required = defaultFormat.toLowerCase().trim();
        format = (DateFormat) formats.get(required);
        if (format == null) {
            setMask(defaultFormat);
        }

        encodingFormat = (DateFormat) formats.get(ISO_ENCODING_FORMAT);
    }

    // //////////////////////////////////////////////////////////////////
    // Parsing
    // //////////////////////////////////////////////////////////////////

    @Override
    protected Object doParse(final Object original, final String entry) {
        final String dateString = entry.trim();
        final String str = dateString.toLowerCase();
        if (str.equals("today") || str.equals("now")) {
            return now();
        } else if (dateString.startsWith("+")) {
            return relativeDate(original == null ? now() : original, dateString, true);
        } else if (dateString.startsWith("-")) {
            return relativeDate(original == null ? now() : original, dateString, false);
        } else {
            return parseDate(dateString, original == null ? now() : original);
        }
    }

    private Object parseDate(final String dateString, final Object original) {
        try {
            return setDate(format.parse(dateString));
        } catch (final ParseException e) {
            final Hashtable formats = formats();
            final Enumeration elements = formats.elements();
            return setDate(parseDate(dateString, elements));
        }
    }

    private Date parseDate(final String dateString, final Enumeration elements) {
        final DateFormat format = (DateFormat) elements.nextElement();
        try {
            return format.parse(dateString);
        } catch (final ParseException e) {
            if (elements.hasMoreElements()) {
                return parseDate(dateString, elements);
            } else {
                throw new TextEntryParseException("Not recognised as a date: " + dateString);
            }
        }
    }

    private Object relativeDate(final Object original, final String str, final boolean add) {
        if (str.equals("")) {
            return now();
        }

        Object date = original;
        final StringTokenizer st = new StringTokenizer(str.substring(1), " ");
        while (st.hasMoreTokens()) {
            final String token = st.nextToken();
            date = relativeDate2(date, token, add);
        }
        return date;
    }

    private Object relativeDate2(final Object original, String str, final boolean add) {
        int hours = 0;
        int minutes = 0;
        int days = 0;
        int months = 0;
        int years = 0;

        if (str.endsWith("H")) {
            str = str.substring(0, str.length() - 1);
            hours = Integer.valueOf(str).intValue();
        } else if (str.endsWith("M")) {
            str = str.substring(0, str.length() - 1);
            minutes = Integer.valueOf(str).intValue();
        } else if (str.endsWith("w")) {
            str = str.substring(0, str.length() - 1);
            days = 7 * Integer.valueOf(str).intValue();
        } else if (str.endsWith("y")) {
            str = str.substring(0, str.length() - 1);
            years = Integer.valueOf(str).intValue();
        } else if (str.endsWith("m")) {
            str = str.substring(0, str.length() - 1);
            months = Integer.valueOf(str).intValue();
        } else if (str.endsWith("d")) {
            str = str.substring(0, str.length() - 1);
            days = Integer.valueOf(str).intValue();
        } else {
            days = Integer.valueOf(str).intValue();
        }

        if (add) {
            return add(original, years, months, days, hours, minutes);
        } else {
            return add(original, -years, -months, -days, -hours, -minutes);
        }
    }

    // ///////////////////////////////////////////////////////////////////////////
    // TitleProvider
    // ///////////////////////////////////////////////////////////////////////////

    @Override
    public String titleString(final Object value) {
        if (value == null) {
            return null;
        }
        final Date date = dateValue(value);
        return titleString(format, date);
    }

    @Override
    public String titleStringWithMask(final Object value, final String usingMask) {
        final Date date = dateValue(value);
        return titleString(new SimpleDateFormat(usingMask), date);
    }

    private String titleString(final DateFormat formatter, final Date date) {
        return date == null ? "" : formatter.format(date);
    }

    // //////////////////////////////////////////////////////////////////
    // EncoderDecoder
    // //////////////////////////////////////////////////////////////////

    @Override
    protected String doEncode(final Object object) {
        final Date date = dateValue(object);
        return encodingFormat.format(date);
    }

    @Override
    protected Object doRestore(final String data) {
        try {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(encodingFormat.parse(data));
            clearFields(cal);
            return setDate(cal.getTime());
        } catch (final ParseException e) {
            throw new EncodingException(e);
        }
    }

    // //////////////////////////////////////////////////////////////////
    // DateValueFacet
    // //////////////////////////////////////////////////////////////////

    public final Date dateValue(final NakedObject object) {
        return object == null ? null : dateValue(object.getObject());
    }

    public final NakedObject createValue(final Date date) {
        return getRuntimeContext().adapterFor(setDate(date));
    }

    /**
     * For subclasses to implement.
     */
    public abstract int getLevel();

    // //////////////////////////////////////////////////////////////////
    // temporal-specific stuff
    // //////////////////////////////////////////////////////////////////

    protected abstract Object add(Object original, int years, int months, int days, int hours, int minutes);

    protected void clearFields(final Calendar cal) {}

    protected abstract Date dateValue(Object value);

    protected abstract String defaultFormat();

    protected abstract Hashtable formats();

    protected boolean ignoreTimeZone() {
        return false;
    }

    protected abstract Object now();

    protected abstract Object setDate(Date date);

    public void setMask(final String mask) {
        format = new SimpleDateFormat(mask);
        format.setLenient(false);
        format.setTimeZone(UTC_TIME_ZONE);
    }

    protected boolean isEmpty() {
        return false;
    }

}
// Copyright (c) Naked Objects Group Ltd.
