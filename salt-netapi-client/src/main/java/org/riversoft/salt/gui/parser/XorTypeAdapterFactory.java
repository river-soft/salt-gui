package org.riversoft.salt.gui.parser;

import com.google.gson.*;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.riversoft.salt.gui.errors.JsonParsingError;
import org.riversoft.salt.gui.errors.SaltError;
import org.riversoft.salt.gui.utils.SaltErrorUtils;
import org.riversoft.salt.gui.utils.Xor;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * TypeAdaptorFactory creating TypeAdapters for Xor
 */
public class XorTypeAdapterFactory implements TypeAdapterFactory {

    private static final Pattern FN_UNAVAILABLE =
            Pattern.compile("'([^']+)' is not available.");
    private static final Pattern MODULE_NOT_SUPPORTED =
            Pattern.compile("'([^']+)' __virtual__ returned False");

    @Override
    @SuppressWarnings("unchecked")
    public <A> TypeAdapter<A> create(Gson gson, TypeToken<A> typeToken) {
        Type type = typeToken.getType();
        boolean isXor = typeToken.getRawType() == Xor.class;
        boolean isParameterized = type instanceof ParameterizedType;
        boolean isSSHResult = ResultSSHResultTypeAdapterFactory.isResultSSHResult(type);
        if (isXor && isParameterized && !isSSHResult) {
            Type rightType = ((ParameterizedType) type).getActualTypeArguments()[1];
            TypeAdapter<?> elementAdapter = gson.getAdapter(TypeToken.get(rightType));
            return (TypeAdapter<A>) errorAdapter(elementAdapter);
        } else {
            return null;
        }
    }

    private <R> TypeAdapter<Xor<SaltError, R>> errorAdapter(TypeAdapter<R> innerAdapter) {
        return new TypeAdapter<Xor<SaltError, R>>() {
            @Override
            public Xor<SaltError, R> read(JsonReader in) throws IOException {
                JsonElement json = TypeAdapters.JSON_ELEMENT.read(in);
                try {
                    R value = innerAdapter.fromJsonTree(json);
                    return Xor.right(value);
                } catch (Throwable e) {
                    Optional<SaltError> saltError =
                            extractErrorString(json).flatMap(SaltErrorUtils::deriveError);
                    return Xor.left(saltError.orElse(new JsonParsingError(json, e)));
                }
            }

            private Optional<String> extractErrorString(JsonElement json) {
                if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
                    return Optional.of(json.getAsJsonPrimitive().getAsString());
                }
                return Optional.empty();
            }

            @Override
            public void write(JsonWriter out, Xor<SaltError, R> xor) throws IOException {
                throw new JsonParseException("Writing Xor is not supported");
            }
        };
    }
}
