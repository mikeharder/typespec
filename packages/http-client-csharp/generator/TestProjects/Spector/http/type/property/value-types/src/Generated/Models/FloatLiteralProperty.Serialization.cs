// <auto-generated/>

#nullable disable

using System;
using System.ClientModel;
using System.ClientModel.Primitives;
using System.Text.Json;

namespace _Type.Property.ValueTypes
{
    public partial class FloatLiteralProperty : IJsonModel<FloatLiteralProperty>
    {
        void IJsonModel<FloatLiteralProperty>.Write(Utf8JsonWriter writer, ModelReaderWriterOptions options) => throw null;

        protected virtual void JsonModelWriteCore(Utf8JsonWriter writer, ModelReaderWriterOptions options) => throw null;

        FloatLiteralProperty IJsonModel<FloatLiteralProperty>.Create(ref Utf8JsonReader reader, ModelReaderWriterOptions options) => throw null;

        protected virtual FloatLiteralProperty JsonModelCreateCore(ref Utf8JsonReader reader, ModelReaderWriterOptions options) => throw null;

        BinaryData IPersistableModel<FloatLiteralProperty>.Write(ModelReaderWriterOptions options) => throw null;

        protected virtual BinaryData PersistableModelWriteCore(ModelReaderWriterOptions options) => throw null;

        FloatLiteralProperty IPersistableModel<FloatLiteralProperty>.Create(BinaryData data, ModelReaderWriterOptions options) => throw null;

        protected virtual FloatLiteralProperty PersistableModelCreateCore(BinaryData data, ModelReaderWriterOptions options) => throw null;

        string IPersistableModel<FloatLiteralProperty>.GetFormatFromOptions(ModelReaderWriterOptions options) => throw null;

        public static implicit operator BinaryContent(FloatLiteralProperty floatLiteralProperty) => throw null;

        public static explicit operator FloatLiteralProperty(ClientResult result) => throw null;
    }
}
