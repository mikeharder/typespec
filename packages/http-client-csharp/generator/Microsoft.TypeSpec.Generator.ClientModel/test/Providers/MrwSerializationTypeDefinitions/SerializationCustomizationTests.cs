// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.TypeSpec.Generator.ClientModel.Providers;
using Microsoft.TypeSpec.Generator.Input;
using Microsoft.TypeSpec.Generator.Primitives;
using Microsoft.TypeSpec.Generator.Providers;
using Microsoft.TypeSpec.Generator.Tests.Common;
using NUnit.Framework;

namespace Microsoft.TypeSpec.Generator.ClientModel.Tests.Providers.MrwSerializationTypeDefinitions
{
    public class SerializationCustomizationTests
    {
        [Test]
        public async Task CanChangePropertyName()
        {
            var props = new[]
            {
                InputFactory.Property("Prop1", InputFactory.Array(InputPrimitiveType.String))
            };

            var inputModel = InputFactory.Model("Model", properties: props, usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);
            Assert.AreEqual(0, serializationProvider!.Fields.Count);

            // validate the methods use the custom member name
            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }

        [Test]
        public async Task ReadOnlyMemPropertyType()
        {
            var inputModel = InputFactory.Model("Model", properties: [
                    InputFactory.Property("Prop1", InputFactory.Array(InputPrimitiveType.String)),
                    InputFactory.Property("Prop2", InputFactory.Array(InputPrimitiveType.String))
                    ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);
            Assert.AreEqual(0, serializationProvider!.Fields.Count);

            // validate the methods use the custom member name
            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }

        [Test]
        public async Task CanCustomizeSerializationMethod()
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                        InputFactory.Property("Prop1", InputPrimitiveType.String),
                        InputFactory.Property("Prop2", new InputNullableType(InputPrimitiveType.String))
                    ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);
            Assert.AreEqual(0, serializationProvider!.Fields.Count);

            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }

        [Test]
        public async Task CanCustomizeSerializationMethodForRenamedProperty()
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                    InputFactory.Property("Prop1", InputPrimitiveType.String),
                    InputFactory.Property("Prop2", new InputNullableType(InputPrimitiveType.String))
                    ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);
            Assert.AreEqual(0, serializationProvider!.Fields.Count);

            // validate the methods use the custom member name
            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }

        // Validates that the custom serialization method is used in the serialization provider
        // for the custom property that exists in the base model.
        [Test]
        public async Task CanCustomizeSerializationMethodForPropertyInBase()
        {
            var baseModel = InputFactory.Model(
                "baseModel",
                usage: InputModelTypeUsage.Input,
                properties: [InputFactory.Property("Prop1", InputPrimitiveType.Int32, isRequired: true)]);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [
                    InputFactory.Model(
                        "mockInputModel",
                        usage: InputModelTypeUsage.Json,
                        properties:
                        [
                            InputFactory.Property("OtherProp", InputPrimitiveType.Int32, isRequired: true),
                        ],
                        baseModel: baseModel),
                ],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.FirstOrDefault(t => t is ModelProvider);
            Assert.IsNotNull(modelProvider);
            var serializationProvider = modelProvider!.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);

            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }

        // Validates that a properties serialization name can be changed using custom code.
        [Test]
        public async Task CanChangePropertySerializedName()
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                    InputFactory.Property("Name", InputPrimitiveType.String),
                    InputFactory.Property("Color", InputPrimitiveType.String),
                    InputFactory.Property("Flavor", InputPrimitiveType.String)
                    ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);

            Assert.IsNotNull(modelProvider);
            Assert.IsNotNull(serializationProvider);

            var properties = modelProvider.Properties;
            Assert.AreEqual(2, properties.Count);
            Assert.AreEqual("Name", properties[0].Name);
            Assert.AreEqual("customName", properties[0].WireInfo?.SerializedName);
            Assert.AreEqual("Flavor", properties[1].Name);
            Assert.AreEqual("flavor", properties[1].WireInfo?.SerializedName);


            var customCodeView = modelProvider.CustomCodeView;
            Assert.IsNotNull(customCodeView);
            var customProperties = customCodeView!.Properties;
            Assert.AreEqual(1, customProperties.Count);
            Assert.AreEqual("CustomColor", customProperties[0].Name);
            Assert.AreEqual("customColor2", customProperties[0].WireInfo?.SerializedName);

            // validate the serialization provider uses the custom property name
            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }

        [Test]
        public async Task CanChangeDictionaryToBinaryData()
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                    // generated type is a dictionary of string to BinaryData
                    InputFactory.Property("Prop1", InputFactory.Dictionary(InputPrimitiveType.Any))
                    ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);
            Assert.AreEqual(0, serializationProvider!.Fields.Count);

            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }

        private static IEnumerable<TestCaseData> ExtensibleEnumCases =>
        [
            new TestCaseData(InputFactory.StringEnum("EnumType", [("value", "value")], isExtensible: true)),
            new TestCaseData(InputFactory.Int32Enum("EnumType", [("one", 1)], isExtensible: true)),
        ];

        [TestCaseSource(nameof(ExtensibleEnumCases))]
        public async Task CanCustomizeExtensibleEnum(InputEnumType enumType)
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                    InputFactory.Property("Prop1", enumType)
                    ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync(enumType.ValueType.Name));

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);
            Assert.AreEqual(0, serializationProvider.Fields.Count);

            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(enumType.ValueType.Name), file.Content);
        }

        private static IEnumerable<TestCaseData> ExtensibleEnumCasesFromLiteral =>
        [
            new TestCaseData(InputFactory.Literal.String("foo", name: "EnumType")),
            new TestCaseData(InputFactory.Literal.Int32(1, name: "EnumType")),
        ];

        [TestCaseSource(nameof(ExtensibleEnumCasesFromLiteral))]
        public async Task CanCustomizeLiteralExtensibleEnum(InputLiteralType literal)
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                    InputFactory.Property("Prop1", literal)
                    ],
                usage: InputModelTypeUsage.Json);
            var parameters = $"{literal.ValueType.Name},{literal.Value}";
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputLiterals: () => [literal],
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync(parameters));

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);
            Assert.AreEqual(0, serializationProvider.Fields.Count);

            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(parameters), file.Content);
        }

        [Test]
        public async Task CanReplaceSerializationMethod()
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                    InputFactory.Property("Prop1", InputPrimitiveType.String),
                    InputFactory.Property("Prop2", new InputNullableType(InputPrimitiveType.String))
                    ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);

            var methods = serializationProvider!.Methods;
            Assert.AreEqual(9, methods.Count);

            // validate the serialization method doesn't exist in the serialization provider
            Assert.IsNull(methods.FirstOrDefault(m => m.Signature.Name == "JsonModelWriteCore"));

            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }

        [Test]
        public async Task CanReplaceWriteMethod()
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                    InputFactory.Property("Prop1", InputPrimitiveType.String),
                    InputFactory.Property("Prop2", new InputNullableType(InputPrimitiveType.String))
                ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);

            var methods = serializationProvider!.Methods;
            Assert.AreEqual(8, methods.Count);

            // validate the Write method doesn't exist in the serialization provider
            Assert.IsNull(methods.FirstOrDefault(m => m.Signature.Name == "Write"));
        }

        [Test]
        public async Task CanReplaceDeserializationMethod()
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                    InputFactory.Property("Prop1", InputPrimitiveType.String),
                    InputFactory.Property("Prop2", new InputNullableType(InputPrimitiveType.String))
                    ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);

            var methods = serializationProvider!.Methods;
            Assert.AreEqual(9, methods.Count);

            // validate the deserialization method doesn't exist in the serialization provider
            Assert.IsNull(methods.FirstOrDefault(m => m.Signature.Name == "DeserializeMockInputModel"));

            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }

        [TestCase(true)]
        [TestCase(false)]
        public async Task CanCustomizePropertyUsingField(bool redefineProperty)
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                    InputFactory.Property("Prop1", InputPrimitiveType.String),
                    ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync(redefineProperty.ToString()));

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);
            Assert.AreEqual(0, modelProvider.Properties.Count);

            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);

            var fullCtor = modelProvider.Constructors.Last();
            Assert.IsTrue(fullCtor.Signature.Modifiers.HasFlag(MethodSignatureModifiers.Internal));
            Assert.AreEqual(2, fullCtor.Signature.Parameters.Count);
        }

        [Test]
        public async Task CanChangeToNonNullableProp()
        {
            var inputModel = InputFactory.Model("Model", properties: [
                    InputFactory.Property("Prop1", new InputNullableType(InputPrimitiveType.String))
                    ],
                usage: InputModelTypeUsage.Json);
            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);

            var deserializationMethod = serializationProvider!.Methods.Where(m => m.Signature.Name.StartsWith("Deserialize")).FirstOrDefault();
            Assert.IsNotNull(deserializationMethod);

            var deserializationStatements = deserializationMethod!.BodyStatements;

            Assert.IsTrue(deserializationStatements!.ToDisplayString().Contains(
                "if ((prop.Value.ValueKind == global::System.Text.Json.JsonValueKind.Null))"));
            // since the customized property is non-nullable, the assignment to null should not be present
            Assert.IsFalse(deserializationStatements!.ToDisplayString().Contains("prop1 = null;"));
        }

        [Test]
        public async Task CanChangeListOfEnumPropToListOfExtensibleEnum()
        {
            var inputModel = InputFactory.Model("Model", properties: [
                    InputFactory.Property("Prop1", InputFactory.Array(InputFactory.StringEnum(
                        "MyEnum",
                        [("foo", "bar")],
                        usage: InputModelTypeUsage.Input)))
                    ]);

            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => [inputModel],
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);
            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }

        [Test]
        public async Task CanChangePropertyNameAndRedefineOriginal()
        {
            var inputModel = InputFactory.Model("mockInputModel", properties: [
                    InputFactory.Property("prop1", InputFactory.Array(InputPrimitiveType.String))
                ]);

            var mockGenerator = await MockHelpers.LoadMockGeneratorAsync(
                inputModels: () => new[] { inputModel },
                compilation: async () => await Helpers.GetCompilationFromDirectoryAsync());

            var modelProvider = mockGenerator.Object.OutputLibrary.TypeProviders.Single(t => t is ModelProvider);
            var serializationProvider = modelProvider.SerializationProviders.Single(t => t is MrwSerializationTypeDefinition);
            Assert.IsNotNull(serializationProvider);
            var writer = new TypeProviderWriter(serializationProvider);
            var file = writer.Write();
            Assert.AreEqual(Helpers.GetExpectedFromFile(), file.Content);
        }
    }
}
