package org.riversoft.salt.gui.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.riversoft.salt.gui.calls.ScheduledJob;
import org.riversoft.salt.gui.calls.wheel.Key;
import org.riversoft.salt.gui.datatypes.*;
import org.riversoft.salt.gui.datatypes.cherrypy.Stats;
import org.riversoft.salt.gui.results.Result;
import org.riversoft.salt.gui.results.ResultInfoSet;
import org.riversoft.salt.gui.results.Return;
import org.riversoft.salt.gui.results.SSHRawResult;
import org.riversoft.salt.gui.utils.ClientUtils;
;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Parser for Salt API responses.
 *
 * @param <T> The result type this parser produces.
 */
public class JsonParser<T> {

    public static final Gson GSON = new GsonBuilder()
    // null rejecting strict variants for primitives
            .registerTypeAdapter(String.class, Adapters.STRING)
            .registerTypeAdapter(Boolean.class, Adapters.BOOLEAN)
            .registerTypeAdapter(Integer.class, Adapters.INTEGER)
            .registerTypeAdapter(Long.class, Adapters.LONG)
            .registerTypeAdapter(Double.class, Adapters.DOUBLE)
            .registerTypeAdapter(Date.class, new DateAdapter().nullSafe())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeISOAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeISOAdapter())
            .registerTypeAdapter(StartTime.class, new StartTimeAdapter().nullSafe())
            .registerTypeAdapter(Stats.class, new StatsAdapter())
            .registerTypeAdapter(Arguments.class, new ArgumentsAdapter())
            .registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
            .registerTypeAdapterFactory(new ResultSSHResultTypeAdapterFactory())
            .registerTypeAdapterFactory(new XorTypeAdapterFactory())
            .registerTypeAdapterFactory(new ResultTypeAdapterFactory())
            //TODO разобраться что тут
//            .registerTypeAdapterFactory(new CollectionTypeAdapterFactory())
            .create();

    public static final JsonParser<Return<String>> STRING =
            new JsonParser<>(new TypeToken<Return<String>>(){});
    public static final JsonParser<Return<List<Token>>> TOKEN =
            new JsonParser<>(new TypeToken<Return<List<Token>>>(){});
    public static final JsonParser<Return<List<ScheduledJob>>> SCHEDULED_JOB =
            new JsonParser<>(new TypeToken<Return<List<ScheduledJob>>>(){});
    public static final JsonParser<Return<List<Map<String, Job>>>> JOBS =
            new JsonParser<>(new TypeToken<Return<List<Map<String, Job>>>>(){});
    public static final JsonParser<ResultInfoSet> JOB_RESULTS =
            new JsonParser<>(new TypeToken<ResultInfoSet>(){});
    public static final JsonParser<Return<List<Map<String, Map<String, Object>>>>> RETMAPS =
            new JsonParser<>(
                    new TypeToken<Return<List<Map<String, Map<String, Object>>>>>(){});
    public static final JsonParser<Return<List<Map<String, Object>>>> RUN_RESULTS =
            new JsonParser<>(new TypeToken<Return<List<Map<String, Object>>>>(){});
    public static final JsonParser<Return<List<Map<String, Result<SSHRawResult>>>>>
    RUNSSHRAW_RESULTS = new JsonParser<>(
    new TypeToken<Return<List<Map<String, Result<SSHRawResult>>>>>() { });
    public static final JsonParser<Stats> STATS =
            new JsonParser<>(new TypeToken<Stats>(){});
    public static final JsonParser<Return<Key.Names>> KEYS =
            new JsonParser<>(new TypeToken<Return<Key.Names>>(){});
    public static final JsonParser<Map<String, Object>> MAP =
            new JsonParser<>(new TypeToken<Map<String, Object>>(){});
    public static final JsonParser<Event> EVENTS =
            new JsonParser<>(new TypeToken<Event>(){});

    private final TypeToken<T> type;
    private final Gson gson;

    /**
     * Created a new JsonParser for the given type.
     *
     * @param type A TypeToken describing the type this parser produces.
     */
    public JsonParser(TypeToken<T> type) {
        this(type, GSON);
    }

    /**
     * Created a new JsonParser for the given type.
     *
     * @param type A TypeToken describing the type this parser produces.
     * @param gson Gson instance to use for parsing.
     */
    public JsonParser(TypeToken<T> type, Gson gson) {
        this.type = type;
        this.gson = gson;
    }

    /**
     * Parses a Json response that has a direct representation as a Java class.
     * @param inputStream result stream to parse.
     * @return The parsed value.
     */
    public T parse(InputStream inputStream) {
        String response = ClientUtils.streamToString(inputStream);
        inputStream = ClientUtils.stringToStream(response);
        Reader inputStreamReader = new InputStreamReader(inputStream);
        Reader streamReader = new BufferedReader(inputStreamReader);

        try {
            // Parse result type from the returned JSON
            return gson.fromJson(streamReader, type.getType());
        } catch (JsonParseException ex) {
            Map<String, Object> originalException = gson.fromJson(response, MAP.type.getType());
            System.out.println("\n Original Salt Response-->\n" + originalException.values() + "\n");

            throw ex;
        }
    }

    public class TargetTypeAdapter extends TypeAdapter<String> {
        @Override
        public void write(JsonWriter out, String value) throws IOException {
            throw new UnsupportedOperationException("Writing JSON not supported.");
        }

        @Override
        public String read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                return null;
            } else if (in.peek() == JsonToken.STRING) {
                return in.nextString();
            } else if (in.peek() == JsonToken.BEGIN_ARRAY) {
                System.out.println("Encountered empty array as a target-type, " +
                        "returning null.");
                in.skipValue();
                return null;
            } else {
                throw new IllegalStateException("Target-type must be either null," +
                        " string or an array.");
            }
        }
    }

    /**
     * Parse JSON given as string.
     * @param jsonString JSON input given as string
     * @return The parsed object
     */
    public T parse(String jsonString) {
        return gson.fromJson(jsonString, type.getType());
    }

}

