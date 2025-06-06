// <auto-generated/>

#nullable disable

using System;
using System.ClientModel;
using System.ClientModel.Primitives;
using System.Text.Json;

namespace Parameters.BodyOptionality
{
    public partial class BodyModel : IJsonModel<BodyModel>
    {
        internal BodyModel() => throw null;

        void IJsonModel<BodyModel>.Write(Utf8JsonWriter writer, ModelReaderWriterOptions options) => throw null;

        protected virtual void JsonModelWriteCore(Utf8JsonWriter writer, ModelReaderWriterOptions options) => throw null;

        BodyModel IJsonModel<BodyModel>.Create(ref Utf8JsonReader reader, ModelReaderWriterOptions options) => throw null;

        protected virtual BodyModel JsonModelCreateCore(ref Utf8JsonReader reader, ModelReaderWriterOptions options) => throw null;

        BinaryData IPersistableModel<BodyModel>.Write(ModelReaderWriterOptions options) => throw null;

        protected virtual BinaryData PersistableModelWriteCore(ModelReaderWriterOptions options) => throw null;

        BodyModel IPersistableModel<BodyModel>.Create(BinaryData data, ModelReaderWriterOptions options) => throw null;

        protected virtual BodyModel PersistableModelCreateCore(BinaryData data, ModelReaderWriterOptions options) => throw null;

        string IPersistableModel<BodyModel>.GetFormatFromOptions(ModelReaderWriterOptions options) => throw null;

        public static implicit operator BinaryContent(BodyModel bodyModel) => throw null;
    }
}
