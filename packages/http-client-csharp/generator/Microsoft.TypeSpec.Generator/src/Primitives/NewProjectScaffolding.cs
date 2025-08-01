// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Threading.Tasks;

namespace Microsoft.TypeSpec.Generator.Primitives
{
    //TODO Need to define the rest of the extensibility points https://github.com/microsoft/typespec/issues/4065
    public class NewProjectScaffolding
    {
        public async Task<bool> Execute()
        {
            //clean up old sln and csproj files
            foreach (var file in Directory.GetFiles(CodeModelGenerator.Instance.Configuration.OutputDirectory, "*.csproj", SearchOption.AllDirectories))
            {
                File.Delete(file);
            }
            foreach (var file in Directory.GetFiles(CodeModelGenerator.Instance.Configuration.OutputDirectory, "*.sln", SearchOption.TopDirectoryOnly))
            {
                File.Delete(file);
            }

            await WriteSolutionFiles();

            await WriteProjectFiles();

            return true;
        }

        private async Task WriteProjectFiles()
        {
            await File.WriteAllBytesAsync(
                Path.Combine(CodeModelGenerator.Instance.Configuration.ProjectDirectory, $"{CodeModelGenerator.Instance.Configuration.PackageName}.csproj"),
                Encoding.UTF8.GetBytes(NormalizeLineEndings(GetSourceProjectFileContent())));
        }

        private string NormalizeLineEndings(string content)
        {
            return content.Replace("\\r\\n", "\\n", StringComparison.Ordinal);
        }

        private async Task WriteSolutionFiles()
        {
            await File.WriteAllBytesAsync(
                Path.Combine(CodeModelGenerator.Instance.Configuration.OutputDirectory, $"{CodeModelGenerator.Instance.Configuration.PackageName}.sln"),
                Encoding.UTF8.GetBytes(NormalizeLineEndings(GetSolutionFileContent())));
        }

        protected virtual string GetSourceProjectFileContent()
        {
            var builder = new CSharpProjectWriter()
            {
                Description = $"This is the {CodeModelGenerator.Instance.Configuration.PackageName} client library for developing .NET applications with rich experience.",
                AssemblyTitle = $"SDK Code Generation {CodeModelGenerator.Instance.Configuration.PackageName}",
                Version = "1.0.0-beta.1",
                PackageTags = CodeModelGenerator.Instance.Configuration.PackageName,
                TargetFrameworks = "netstandard2.0;net8.0",
                LangVersion = "latest",
                GenerateDocumentationFile = true,
            };
            foreach (var packages in _unbrandedDependencyPackages)
            {
                builder.PackageReferences.Add(packages);
            }
            foreach (var compileInclude in CompileIncludes)
            {
                builder.CompileIncludes.Add(compileInclude);
            }

            return builder.Write();
        }

        private IReadOnlyList<CSharpProjectCompileInclude>? _compileIncludes;
        public IReadOnlyList<CSharpProjectCompileInclude> CompileIncludes => _compileIncludes ??= BuildCompileIncludes();

        protected virtual IReadOnlyList<CSharpProjectCompileInclude> BuildCompileIncludes() => [];

        private static readonly IReadOnlyList<CSharpProjectWriter.CSProjDependencyPackage> _unbrandedDependencyPackages = new CSharpProjectWriter.CSProjDependencyPackage[]
        {
            new("System.ClientModel", "1.5.0"),
        };

        protected virtual string GetSolutionFileContent()
        {
            string slnContent = @"Microsoft Visual Studio Solution File, Format Version 12.00
# Visual Studio Version 17
VisualStudioVersion = 17.0.31903.59
MinimumVisualStudioVersion = 10.0.40219.1
";
            slnContent += @"Project(""{{FAE04EC0-301F-11D3-BF4B-00C04F79EFBC}}"") = ""{0}"", ""src\{0}.csproj"", ""{{28FF4005-4467-4E36-92E7-DEA27DEB1519}}""
EndProject
";
            slnContent += @"Global
	GlobalSection(SolutionConfigurationPlatforms) = preSolution
		Debug|Any CPU = Debug|Any CPU
		Release|Any CPU = Release|Any CPU
	EndGlobalSection
	GlobalSection(ProjectConfigurationPlatforms) = postSolution
		{{B0C276D1-2930-4887-B29A-D1A33E7009A2}}.Debug|Any CPU.ActiveCfg = Debug|Any CPU
		{{B0C276D1-2930-4887-B29A-D1A33E7009A2}}.Debug|Any CPU.Build.0 = Debug|Any CPU
		{{B0C276D1-2930-4887-B29A-D1A33E7009A2}}.Release|Any CPU.ActiveCfg = Release|Any CPU
		{{B0C276D1-2930-4887-B29A-D1A33E7009A2}}.Release|Any CPU.Build.0 = Release|Any CPU
		{{8E9A77AC-792A-4432-8320-ACFD46730401}}.Debug|Any CPU.ActiveCfg = Debug|Any CPU
		{{8E9A77AC-792A-4432-8320-ACFD46730401}}.Debug|Any CPU.Build.0 = Debug|Any CPU
		{{8E9A77AC-792A-4432-8320-ACFD46730401}}.Release|Any CPU.ActiveCfg = Release|Any CPU
		{{8E9A77AC-792A-4432-8320-ACFD46730401}}.Release|Any CPU.Build.0 = Release|Any CPU
";
            slnContent += @"		{{A4241C1F-A53D-474C-9E4E-075054407E74}}.Debug|Any CPU.ActiveCfg = Debug|Any CPU
		{{A4241C1F-A53D-474C-9E4E-075054407E74}}.Debug|Any CPU.Build.0 = Debug|Any CPU
		{{A4241C1F-A53D-474C-9E4E-075054407E74}}.Release|Any CPU.ActiveCfg = Release|Any CPU
		{{A4241C1F-A53D-474C-9E4E-075054407E74}}.Release|Any CPU.Build.0 = Release|Any CPU
		{{FA8BD3F1-8616-47B6-974C-7576CDF4717E}}.Debug|Any CPU.ActiveCfg = Debug|Any CPU
		{{FA8BD3F1-8616-47B6-974C-7576CDF4717E}}.Debug|Any CPU.Build.0 = Debug|Any CPU
		{{FA8BD3F1-8616-47B6-974C-7576CDF4717E}}.Release|Any CPU.ActiveCfg = Release|Any CPU
		{{FA8BD3F1-8616-47B6-974C-7576CDF4717E}}.Release|Any CPU.Build.0 = Release|Any CPU
		{{85677AD3-C214-42FA-AE6E-49B956CAC8DC}}.Debug|Any CPU.ActiveCfg = Debug|Any CPU
		{{85677AD3-C214-42FA-AE6E-49B956CAC8DC}}.Debug|Any CPU.Build.0 = Debug|Any CPU
		{{85677AD3-C214-42FA-AE6E-49B956CAC8DC}}.Release|Any CPU.ActiveCfg = Release|Any CPU
		{{85677AD3-C214-42FA-AE6E-49B956CAC8DC}}.Release|Any CPU.Build.0 = Release|Any CPU
		{{28FF4005-4467-4E36-92E7-DEA27DEB1519}}.Debug|Any CPU.ActiveCfg = Debug|Any CPU
		{{28FF4005-4467-4E36-92E7-DEA27DEB1519}}.Debug|Any CPU.Build.0 = Debug|Any CPU
		{{28FF4005-4467-4E36-92E7-DEA27DEB1519}}.Release|Any CPU.ActiveCfg = Release|Any CPU
		{{28FF4005-4467-4E36-92E7-DEA27DEB1519}}.Release|Any CPU.Build.0 = Release|Any CPU
		{{1F1CD1D4-9932-4B73-99D8-C252A67D4B46}}.Debug|Any CPU.ActiveCfg = Debug|Any CPU
		{{1F1CD1D4-9932-4B73-99D8-C252A67D4B46}}.Debug|Any CPU.Build.0 = Debug|Any CPU
		{{1F1CD1D4-9932-4B73-99D8-C252A67D4B46}}.Release|Any CPU.ActiveCfg = Release|Any CPU
		{{1F1CD1D4-9932-4B73-99D8-C252A67D4B46}}.Release|Any CPU.Build.0 = Release|Any CPU
	EndGlobalSection
	GlobalSection(SolutionProperties) = preSolution
		HideSolutionNode = FALSE
	EndGlobalSection
	GlobalSection(ExtensibilityGlobals) = postSolution
		SolutionGuid = {{A97F4B90-2591-4689-B1F8-5F21FE6D6CAE}}
	EndGlobalSection
EndGlobal
";
            return string.Format(slnContent, CodeModelGenerator.Instance.Configuration.PackageName);
        }
    }
}
