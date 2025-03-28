// <auto-generated/>

#nullable disable

using System;
using System.ClientModel;
using System.ClientModel.Primitives;
using System.Threading;
using System.Threading.Tasks;

namespace Versioning.RenamedFrom
{
    public partial class RenamedFromClient
    {
        protected RenamedFromClient() => throw null;

        public RenamedFromClient(Uri endpoint) : this(endpoint, new RenamedFromClientOptions()) => throw null;

        public RenamedFromClient(Uri endpoint, RenamedFromClientOptions options) => throw null;

        public ClientPipeline Pipeline => throw null;

        public virtual ClientResult OldOp(string oldQuery, BinaryContent content, RequestOptions options = null) => throw null;

        public virtual Task<ClientResult> OldOpAsync(string oldQuery, BinaryContent content, RequestOptions options = null) => throw null;

        public virtual ClientResult<OldModel> OldOp(string oldQuery, OldModel body, CancellationToken cancellationToken = default) => throw null;

        public virtual Task<ClientResult<OldModel>> OldOpAsync(string oldQuery, OldModel body, CancellationToken cancellationToken = default) => throw null;

        public virtual OldInterface GetOldInterfaceClient() => throw null;
    }
}
