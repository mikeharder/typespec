// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

using System;
using System.ClientModel;
using System.ClientModel.Primitives;
using System.IO;
using System.Text;
using System.Text.Json;


namespace Microsoft.TypeSpec.Generator.Tests.Common
{
    public abstract class RoundTripStrategy<T>
    {
        public abstract object Read(string payload, object model, ModelReaderWriterOptions options);
        public abstract BinaryData Write(T model, ModelReaderWriterOptions options);
        public abstract bool IsExplicitJsonWrite { get; }
        public abstract bool IsExplicitJsonRead { get; }

        protected BinaryData WriteWithJsonInterface<U>(IJsonModel<U> model, ModelReaderWriterOptions options)
        {
            using MemoryStream stream = new MemoryStream();
            using Utf8JsonWriter writer = new Utf8JsonWriter(stream);
            model.Write(writer, options);
            writer.Flush();
            if (stream.Position > int.MaxValue)
            {
                return BinaryData.FromStream(stream);
            }
            else
            {
                return new BinaryData(stream.GetBuffer().AsMemory(0, (int)stream.Position));
            }
        }
    }

    public class ModelReaderWriterStrategy<T> : RoundTripStrategy<T> where T : IPersistableModel<T>
    {
        public override bool IsExplicitJsonWrite => false;
        public override bool IsExplicitJsonRead => false;

        public override BinaryData Write(T model, ModelReaderWriterOptions options)
        {
            return ModelReaderWriter.Write(model, options);
        }
        public override object Read(string payload, object model, ModelReaderWriterOptions options)
        {
            return ModelReaderWriter.Read<T>(new BinaryData(Encoding.UTF8.GetBytes(payload)), options) ?? throw new InvalidOperationException($"Reading model of type {model.GetType().Name} resulted in null");
        }
    }

    public class ModelReaderWriterNonGenericStrategy<T> : RoundTripStrategy<T> where T : IPersistableModel<T>
    {
        public override bool IsExplicitJsonWrite => false;
        public override bool IsExplicitJsonRead => false;

        public override BinaryData Write(T model, ModelReaderWriterOptions options)
        {
            return ModelReaderWriter.Write((object)model, options);
        }

        public override object Read(string payload, object model, ModelReaderWriterOptions options)
        {
            return ModelReaderWriter.Read(new BinaryData(Encoding.UTF8.GetBytes(payload)), typeof(T), options) ?? throw new InvalidOperationException($"Reading model of type {model.GetType().Name} resulted in null");
        }
    }

    public class ModelInterfaceStrategy<T> : RoundTripStrategy<T> where T : IPersistableModel<T>
    {
        public override bool IsExplicitJsonWrite => false;
        public override bool IsExplicitJsonRead => false;

        public override BinaryData Write(T model, ModelReaderWriterOptions options)
        {
            return model.Write(options);
        }

        public override object Read(string payload, object model, ModelReaderWriterOptions options)
        {
            return ((IPersistableModel<T>)model).Create(new BinaryData(Encoding.UTF8.GetBytes(payload)), options)!;
        }
    }

    public class ModelInterfaceAsObjectStrategy<T> : RoundTripStrategy<T> where T : IPersistableModel<T>
    {
        public override bool IsExplicitJsonWrite => false;
        public override bool IsExplicitJsonRead => false;

        public override BinaryData Write(T model, ModelReaderWriterOptions options)
        {
            return ((IPersistableModel<object>)model).Write(options);
        }

        public override object Read(string payload, object model, ModelReaderWriterOptions options)
        {
            return ((IPersistableModel<object>)model).Create(new BinaryData(Encoding.UTF8.GetBytes(payload)), options)!;
        }
    }

    public class JsonInterfaceStrategy<T> : RoundTripStrategy<T> where T : IJsonModel<T>
    {
        public override bool IsExplicitJsonWrite => true;
        public override bool IsExplicitJsonRead => false;

        public override BinaryData Write(T model, ModelReaderWriterOptions options)
        {
            return WriteWithJsonInterface(model, options);
        }

        public override object Read(string payload, object model, ModelReaderWriterOptions options)
        {
            return ((IJsonModel<T>)model).Create(new BinaryData(Encoding.UTF8.GetBytes(payload)), options)!;
        }
    }

    public class JsonInterfaceAsObjectStrategy<T> : RoundTripStrategy<T> where T : IJsonModel<T>
    {
        public override bool IsExplicitJsonWrite => true;
        public override bool IsExplicitJsonRead => false;

        public override BinaryData Write(T model, ModelReaderWriterOptions options)
        {
            return WriteWithJsonInterface((IJsonModel<object>)model, options);
        }

        public override object Read(string payload, object model, ModelReaderWriterOptions options)
        {
            return ((IJsonModel<object>)model).Create(new BinaryData(Encoding.UTF8.GetBytes(payload)), options)!;
        }
    }

    public class JsonInterfaceUtf8ReaderStrategy<T> : RoundTripStrategy<T> where T : IJsonModel<T>
    {
        public override bool IsExplicitJsonWrite => true;
        public override bool IsExplicitJsonRead => true;

        public override BinaryData Write(T model, ModelReaderWriterOptions options)
        {
            return WriteWithJsonInterface(model, options);
        }

        public override object Read(string payload, object model, ModelReaderWriterOptions options)
        {
            var reader = new Utf8JsonReader(new BinaryData(Encoding.UTF8.GetBytes(payload)));
            return ((IJsonModel<T>)model).Create(ref reader, options)!;
        }
    }

    public class JsonInterfaceUtf8ReaderAsObjectStrategy<T> : RoundTripStrategy<T> where T : IJsonModel<T>
    {
        public override bool IsExplicitJsonWrite => true;
        public override bool IsExplicitJsonRead => true;

        public override BinaryData Write(T model, ModelReaderWriterOptions options)
        {
            return WriteWithJsonInterface((IJsonModel<object>)model, options);
        }

        public override object Read(string payload, object model, ModelReaderWriterOptions options)
        {
            var reader = new Utf8JsonReader(new BinaryData(Encoding.UTF8.GetBytes(payload)));
            return ((IJsonModel<object>)model).Create(ref reader, options)!;
        }
    }

    public class CastStrategy<T> : RoundTripStrategy<T> where T : IPersistableModel<T>
    {
        public override bool IsExplicitJsonWrite => false;
        public override bool IsExplicitJsonRead => false;

        private readonly Func<T, BinaryContent> _toBinaryContent;
        private readonly Func<ClientResult, T> _fromResult;

        public CastStrategy(Func<T, BinaryContent> toRequestContent, Func<ClientResult, T> fromResult)
        {
            _toBinaryContent = toRequestContent;
            _fromResult = fromResult;
        }

        public override BinaryData Write(T model, ModelReaderWriterOptions options)
        {
            BinaryContent content = _toBinaryContent(model);
            content.TryComputeLength(out var length);
            using var stream = new MemoryStream((int)length);
            content.WriteTo(stream, default);
            if (stream.Position > int.MaxValue)
            {
                return BinaryData.FromStream(stream);
            }
            else
            {
                return new BinaryData(stream.GetBuffer().AsMemory(0, (int)stream.Position));
            }
        }

        public override object Read(string payload, object model, ModelReaderWriterOptions options)
        {
            var responseWithBody = new TestPipelineResponse(200);
            responseWithBody.SetContent(payload);

            ClientResult result = ClientResult.FromResponse(responseWithBody);
            return _fromResult(result);
        }
    }
}
