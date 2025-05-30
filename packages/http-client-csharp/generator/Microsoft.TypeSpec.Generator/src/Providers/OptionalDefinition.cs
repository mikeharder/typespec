// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

using System.Collections.Generic;
using System.IO;
using Microsoft.TypeSpec.Generator.Expressions;
using Microsoft.TypeSpec.Generator.Primitives;
using Microsoft.TypeSpec.Generator.Snippets;
using Microsoft.TypeSpec.Generator.Statements;
using static Microsoft.TypeSpec.Generator.Snippets.Snippet;

namespace Microsoft.TypeSpec.Generator.Providers
{
    public class OptionalDefinition : TypeProvider
    {
        private class ListTemplate<T> { }

        private readonly CSharpType _t = typeof(ListTemplate<>).GetGenericArguments()[0];
        private readonly CSharpType _tKey;
        private readonly CSharpType _tValue;
        private readonly CSharpType _genericChangeTrackingList;
        private readonly CSharpType _genericChangeTrackingDictionary;

        public OptionalDefinition()
        {
            _genericChangeTrackingList = CodeModelGenerator.Instance.TypeFactory.ListInitializationType;
            _genericChangeTrackingDictionary = CodeModelGenerator.Instance.TypeFactory.DictionaryInitializationType;
            _tKey = _genericChangeTrackingDictionary.Arguments[0];
            _tValue = _genericChangeTrackingDictionary.Arguments[1];
        }

        protected override TypeSignatureModifiers BuildDeclarationModifiers()
        {
            return TypeSignatureModifiers.Internal | TypeSignatureModifiers.Static;
        }

        protected override string BuildRelativeFilePath() => Path.Combine("src", "Generated", "Internal", $"{Name}.cs");

        protected override string BuildName() => "Optional";

        protected override MethodProvider[] BuildMethods()
        {
            return
            [
                BuildIsListDefined(),
                BuildIsDictionaryDefined(),
                BuildIsReadOnlyDictionaryDefined(),
                IsStructDefined(),
                IsObjectDefined(),
                IsStringDefined(),
            ];
        }

        private MethodSignature GetIsDefinedSignature(ParameterProvider valueParam, IReadOnlyList<CSharpType>? genericArguments = null, IReadOnlyList<WhereExpression>? genericParameterConstraints = null) => new(
            "IsDefined",
            null,
            MethodSignatureModifiers.Public | MethodSignatureModifiers.Static,
            typeof(bool),
            null,
            [valueParam],
            GenericArguments: genericArguments,
            GenericParameterConstraints: genericParameterConstraints);

        private MethodSignature GetIsCollectionDefinedSignature(ParameterProvider collectionParam, params CSharpType[] cSharpTypes) => new(
            "IsCollectionDefined",
            null,
            MethodSignatureModifiers.Public | MethodSignatureModifiers.Static,
            typeof(bool),
            null,
            [collectionParam],
            GenericArguments: cSharpTypes);

        private MethodProvider IsStringDefined()
        {
            var valueParam = new ParameterProvider("value", $"The value.", typeof(string));
            var signature = GetIsDefinedSignature(valueParam);
            return new MethodProvider(signature, new MethodBodyStatement[]
            {
                Return(valueParam.NotEqual(Null))
            },
            this,
            XmlDocProvider.Empty);
        }

        private MethodProvider IsObjectDefined()
        {
            var valueParam = new ParameterProvider("value", $"The value.", typeof(object));
            var signature = GetIsDefinedSignature(valueParam);
            return new MethodProvider(signature, new MethodBodyStatement[]
            {
                Return(valueParam.NotEqual(Null))
            },
            this,
            XmlDocProvider.Empty);
        }

        private MethodProvider IsStructDefined()
        {
            var valueParam = new ParameterProvider("value", $"The value.", _t.WithNullable(true));
            var signature = GetIsDefinedSignature(valueParam, [_t], [Where.Struct(_t)]);
            return new MethodProvider(signature, new MethodBodyStatement[]
            {
                Return(new MemberExpression(valueParam, "HasValue"))
            },
            this,
            XmlDocProvider.Empty);
        }

        private MethodProvider BuildIsReadOnlyDictionaryDefined()
        {
            var collectionParam = new ParameterProvider("collection", $"The value.", new CSharpType(typeof(IReadOnlyDictionary<,>), _tKey, _tValue));
            var signature = GetIsCollectionDefinedSignature(collectionParam, _tKey, _tValue);
            VariableExpression changeTrackingReference = new VariableExpression(_genericChangeTrackingDictionary, new CodeWriterDeclaration("changeTrackingDictionary"));
            DeclarationExpression changeTrackingDeclarationExpression = new(changeTrackingReference);

            return new MethodProvider(signature, new MethodBodyStatement[]
            {
                Return(Not(collectionParam.Is(changeTrackingDeclarationExpression)
                    .And(new MemberExpression(changeTrackingReference, "IsUndefined"))))
            },
            this,
            XmlDocProvider.Empty);
        }

        private MethodProvider BuildIsDictionaryDefined()
        {
            var collectionParam = new ParameterProvider("collection", $"The collection.", new CSharpType(typeof(IDictionary<,>), _tKey, _tValue));
            var signature = GetIsCollectionDefinedSignature(collectionParam, _tKey, _tValue);
            VariableExpression changeTrackingReference = new VariableExpression(_genericChangeTrackingDictionary, new CodeWriterDeclaration("changeTrackingDictionary"));
            DeclarationExpression changeTrackingDeclarationExpression = new(changeTrackingReference);

            return new MethodProvider(signature, new MethodBodyStatement[]
            {
                Return(Not(collectionParam.Is(changeTrackingDeclarationExpression)
                    .And(new MemberExpression(changeTrackingReference, "IsUndefined"))))
            },
            this,
            XmlDocProvider.Empty);
        }

        private MethodProvider BuildIsListDefined()
        {
            var collectionParam = new ParameterProvider("collection", $"The collection.", new CSharpType(typeof(IEnumerable<>), _t));
            var signature = GetIsCollectionDefinedSignature(collectionParam, _t);
            VariableExpression changeTrackingReference = new VariableExpression(_genericChangeTrackingList, new CodeWriterDeclaration("changeTrackingList"));
            DeclarationExpression changeTrackingDeclarationExpression = new(changeTrackingReference);

            return new MethodProvider(signature, new MethodBodyStatement[]
            {
                Return(Not(collectionParam.Is(changeTrackingDeclarationExpression)
                    .And(new MemberExpression(changeTrackingReference, "IsUndefined"))))
            },
            this,
            XmlDocProvider.Empty);
        }
    }
}
