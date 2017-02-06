package org.riversoft.salt.gui.calls.runner;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.riversoft.salt.gui.calls.*;
import org.riversoft.salt.gui.datatypes.StartTime;
import org.riversoft.salt.gui.parser.JsonParser;
import org.riversoft.salt.gui.results.Return;

import java.lang.reflect.Type;
import java.util.*;

import static org.riversoft.salt.gui.utils.ClientUtils.parameterizedType;

/**
 * salt.runners.jobs
 */
public class Jobs {

    /**
     * Information about a salt job as returned by 'jobs.list_job'
     */
    public static class Info {

        @SerializedName("Function")
        private String function;

        @SerializedName("StartTime")
        private StartTime startTime;

        @SerializedName("Arguments")
        private List<Object> arguments;

        @SerializedName("User")
        private String user;

        @SerializedName("Target")
        private Object target;

        @SerializedName("Minions")
        private List<String> minions;

        @SerializedName("Target-type")
        private String targetType;

        private String jid;

        @SerializedName("Result")
        private Map<String, JsonElement> result;

        public String getFunction() {
            return function;
        }

        public StartTime getStartTime() {
            return startTime;
        }

        public List<Object> getArguments() {
            return arguments;
        }

        public String getUser() {
            return user;
        }

        public Object getTarget() {
            return target;
        }

        public List<String> getMinions() {
            return minions;
        }

        public String getTargetType() {
            return targetType;
        }

        public String getJid() {
            return jid;
        }

        public <T> Optional<T> getResult(String minionId, Class<T> type) {
            return Optional.ofNullable(result.get(minionId)).map(result -> {
                Type wrapperType = parameterizedType(null, Return.class, type);
                Return<T> r = JsonParser.GSON.fromJson(result, wrapperType);
                return r.getResult();
            });
        }

        public <T> Optional<T> getResult(String minionId, TypeToken<T> type) {
            return Optional.ofNullable(result.get(minionId)).map(result -> {
                Type wrapperType = parameterizedType(null, Return.class, type.getType());
                Return<T> r = JsonParser.GSON.fromJson(result, wrapperType);
                return r.getResult();
            });
        }

    }

    private Jobs() { }

    public static RunnerCall<Map<String, Object>> lookupJid(String jid) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("jid", jid);
        return new RunnerCall<>("jobs.lookup_jid", Optional.of(args),
                new TypeToken<Map<String, Object>>(){});
    }

    @SuppressWarnings("unchecked")
    public static <R> RunnerCall<Map<String, R>> lookupJid(LocalAsyncResult<R> jid) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("jid", jid.getJid());
        Type type = parameterizedType(null, Map.class, String.class,
                jid.getType().getType());
        return new RunnerCall<>("jobs.lookup_jid", Optional.of(args),
                (TypeToken<Map<String, R>>) TypeToken.get(type));
    }

    @SuppressWarnings("unchecked")
    public static <R> RunnerCall<Map<String, Data<R>>> lookupJid(WheelAsyncResult<R> jid) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("jid", jid.getJid());
        Type dataType = parameterizedType(null, Data.class, jid.getType().getType());
        Type type = parameterizedType(null, Map.class, String.class, dataType);
        return new RunnerCall<>("jobs.lookup_jid", Optional.of(args),
                (TypeToken<Map<String, Data<R>>>) TypeToken.get(type));
    }

    @SuppressWarnings("unchecked")
    public static <R> RunnerCall<Map<String, Data<R>>> lookupJid(RunnerAsyncResult<R> jid) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("jid", jid.getJid());
        Type dataType = parameterizedType(null, Data.class, jid.getType().getType());
        Type type = parameterizedType(null, Map.class, String.class, dataType);
        return new RunnerCall<>("jobs.lookup_jid", Optional.of(args),
                (TypeToken<Map<String, Data<R>>>) TypeToken.get(type));
    }

    /**
     * Result entry of jobs.list_jobs
     */
    public static class ListJobsEntry {
        @SerializedName("Function")
        private String function;

        @SerializedName("StartTime")
        private StartTime startTime;

        @SerializedName("Arguments")
        private List<Object> arguments;

        @SerializedName("User")
        private String user;

        @SerializedName("Target")
        private Object target;

        public String getFunction() {
            return function;
        }

        public Date getStartTime(TimeZone tz) {
            return startTime == null ? null : startTime.getDate(tz);
        }

        public Date getStartTime() {
            return startTime == null ? null : startTime.getDate();
        }

        public List<Object> getArguments() {
            return arguments;
        }

        public String getUser() {
            return user;
        }

        public Object getTarget() {
            return target;
        }
    }

    public static RunnerCall<Map<String, ListJobsEntry>> listJobs(Object searchMetadata) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("search_metadata", searchMetadata);
        return new RunnerCall<>("jobs.list_jobs", Optional.of(args),
                new TypeToken<Map<String, ListJobsEntry>>() { });
    }

    public static RunnerCall<Info> listJob(String jid) {
        LinkedHashMap<String, Object> args = new LinkedHashMap<>();
        args.put("jid", jid);
        return new RunnerCall<>("jobs.list_job", Optional.of(args),
                new TypeToken<Info>(){});
    }
}
