// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License. See License.txt in the project root for license information.

export interface Configuration {
  "package-name": string | null;
  "unreferenced-types-handling"?: "removeOrInternalize" | "internalize" | "keepAll";
  "disable-xml-docs"?: boolean;
  license?: {
    name: string;
    company?: string;
    header?: string;
    link?: string;
    description?: string;
  };
  // Any additional properties coming from custom emitter options
  [key: string]: unknown;
}
