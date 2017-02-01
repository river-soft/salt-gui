package org.riversoft.salt.gui.parser;

import com.google.gson.*;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.riversoft.salt.gui.errors.JsonParsingError;
import org.riversoft.salt.gui.errors.SaltError;
import org.riversoft.salt.gui.results.Result;
import org.riversoft.salt.gui.results.SSHResult;
import org.riversoft.salt.gui.utils.SaltErrorUtils;
import org.riversoft.salt.gui.utils.Xor;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * {@link TypeAdapterFactory} for creating type adapters for parsing wrapped
 * Result&lt;SSHResult&gt; objects.
 */
public class ResultSSHResultTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <A> TypeAdapter<A> create(Gson gson, TypeToken<A> typeToken) {
        boolean isResult = typeToken.getRawType() == Result.class;
        Type type = typeToken.getType();
        boolean isSSHResult = isResultSSHResult(type);

        if (isResult && isSSHResult) {
            Type parameterType = ((ParameterizedType) type).getActualTypeArguments()[0];
            TypeAdapter<SSHResult<A>> sshResultAdapter = (TypeAdapter<SSHResult<A>>)
                    gson.getAdapter(TypeToken.get(parameterType));
            return (TypeAdapter<A>) resultAdapter(sshResultAdapter);
        } else {
            return null;
        }
    }

    public static boolean isResultSSHResult(Type type) {
        boolean isParametrized = type instanceof ParameterizedType;

        if (!isParametrized) {
            return false;
        }

        Type[] innerType = ((ParameterizedType) type).getActualTypeArguments();

        return isParametrized && innerType.length > 0 &&
                innerType[0] instanceof ParameterizedType &&
                ((ParameterizedType) innerType[0]).getRawType() == SSHResult.class;
    }

    private <R> TypeAdapter<Result<SSHResult<R>>> resultAdapter(
            TypeAdapter<SSHResult<R>> innerAdapter) {
        return new TypeAdapter<Result<SSHResult<R>>>() {
            @Override
            public Result<SSHResult<R>> read(JsonReader in) throws IOException {
                JsonElement json = TypeAdapters.JSON_ELEMENT.read(in);
                try {
                    SSHResult<R> value = innerAdapter.fromJsonTree(json);
                    if (!value.getReturn().isPresent() && value.getRetcode() != 0) {
                        throw new NullPointerException("No salt ssh return value," +
                                " return code: " + value.getRetcode());
                    }
                    return new Result<>(Xor.right(value));
                } catch (Throwable e) {
                    Optional<SaltError> saltError =
                            extractStdErr(json).flatMap(SaltErrorUtils::deriveError);
                    return new Result<>(Xor.left(
                            saltError.orElse(new JsonParsingError(json, e))));
                }
            }

            @Override
            public void write(JsonWriter out, Result<SSHResult<R>> xor) throws IOException {
                throw new JsonParseException("Writing Xor is not supported");
            }

            private Optional<String> extractStdErr(JsonElement json) {
                if (json.isJsonObject() && json.getAsJsonObject().has("stderr") &&
                        json.getAsJsonObject().get("stderr").isJsonPrimitive() &&
                        json.getAsJsonObject().get("stderr")
                                .getAsJsonPrimitive().isString()) {
                    return Optional.of(json.getAsJsonObject().get("stderr")
                            .getAsJsonPrimitive().getAsString());
                }

                return Optional.empty();
            }
        };
    }

}

