// <auto-generated/>

#nullable disable

using System;
using System.Collections.Generic;

namespace _Type.Model.Inheritance.NestedDiscriminator.Models
{
    public partial class SawShark : Shark
    {
        public SawShark(int age) : base(age, "saw") => throw null;

        internal SawShark(string kind, int age, IDictionary<string, BinaryData> additionalBinaryDataProperties, string sharktype) : base(kind, age, additionalBinaryDataProperties, sharktype) => throw null;
    }
}