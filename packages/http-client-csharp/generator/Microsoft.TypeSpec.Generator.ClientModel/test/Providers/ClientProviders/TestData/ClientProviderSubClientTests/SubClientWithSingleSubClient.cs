// <auto-generated/>

#nullable disable

using System.Threading;

namespace Sample
{
    /// <summary> DogClient description. </summary>
    public partial class Dog
    {
        /// <summary> Initializes a new instance of Husky. </summary>
        public virtual global::Sample.Husky GetHuskyClient()
        {
            return (global::System.Threading.Volatile.Read(ref _cachedHusky) ?? (global::System.Threading.Interlocked.CompareExchange(ref _cachedHusky, new global::Sample.Husky(), null) ?? _cachedHusky));
        }
    }
}
