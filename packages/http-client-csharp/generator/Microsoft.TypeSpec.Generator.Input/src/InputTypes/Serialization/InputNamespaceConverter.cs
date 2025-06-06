// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

using System;
using System.Collections.Generic;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace Microsoft.TypeSpec.Generator.Input
{
    internal sealed class InputNamespaceConverter : JsonConverter<InputNamespace>
    {
        private readonly TypeSpecReferenceHandler _referenceHandler;

        public InputNamespaceConverter(TypeSpecReferenceHandler referenceHandler)
        {
            _referenceHandler = referenceHandler;
        }

        public override InputNamespace? Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
            => ReadInputNamespace(ref reader, options, _referenceHandler.CurrentResolver);

        public override void Write(Utf8JsonWriter writer, InputNamespace value, JsonSerializerOptions options)
            => throw new NotSupportedException("Writing not supported");

        private static InputNamespace? ReadInputNamespace(ref Utf8JsonReader reader, JsonSerializerOptions options, ReferenceResolver resolver)
        {
            if (reader.TokenType == JsonTokenType.StartObject)
            {
                reader.Read();
            }

            string? name = null;
            IReadOnlyList<string>? apiVersions = null;
            IReadOnlyList<InputLiteralType>? constants = null;
            IReadOnlyList<InputEnumType>? enums = null;
            IReadOnlyList<InputModelType>? models = null;
            IReadOnlyList<InputClient>? clients = null;
            InputAuth? auth = null;

            while (reader.TokenType != JsonTokenType.EndObject)
            {
                var isKnownProperty = reader.TryReadString("name", ref name)
                    || reader.TryReadComplexType("apiVersions", options, ref apiVersions)
                    || reader.TryReadComplexType("constants", options, ref constants)
                    || reader.TryReadComplexType("enums", options, ref enums)
                    || reader.TryReadComplexType("models", options, ref models)
                    || reader.TryReadComplexType("clients", options, ref clients)
                    || reader.TryReadComplexType("auth", options, ref auth);

                if (!isKnownProperty)
                {
                    reader.SkipProperty();
                }
            }

            apiVersions ??= [];
            constants ??= [];
            enums ??= [];
            models ??= [];
            clients ??= [];

            return new InputNamespace(
                name ?? throw new JsonException(),
                apiVersions,
                constants,
                enums,
                models,
                clients,
                auth);
        }
    }
}
