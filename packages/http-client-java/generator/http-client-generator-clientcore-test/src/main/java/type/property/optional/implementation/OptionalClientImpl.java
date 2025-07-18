package type.property.optional.implementation;

import io.clientcore.core.http.pipeline.HttpPipeline;
import io.clientcore.core.instrumentation.Instrumentation;

/**
 * Initializes a new instance of the OptionalClient type.
 */
public final class OptionalClientImpl {
    /**
     * Service host.
     */
    private final String endpoint;

    /**
     * Gets Service host.
     * 
     * @return the endpoint value.
     */
    public String getEndpoint() {
        return this.endpoint;
    }

    /**
     * The HTTP pipeline to send requests through.
     */
    private final HttpPipeline httpPipeline;

    /**
     * Gets The HTTP pipeline to send requests through.
     * 
     * @return the httpPipeline value.
     */
    public HttpPipeline getHttpPipeline() {
        return this.httpPipeline;
    }

    /**
     * The instance of instrumentation to report telemetry.
     */
    private final Instrumentation instrumentation;

    /**
     * Gets The instance of instrumentation to report telemetry.
     * 
     * @return the instrumentation value.
     */
    public Instrumentation getInstrumentation() {
        return this.instrumentation;
    }

    /**
     * The StringOperationsImpl object to access its operations.
     */
    private final StringOperationsImpl stringOperations;

    /**
     * Gets the StringOperationsImpl object to access its operations.
     * 
     * @return the StringOperationsImpl object.
     */
    public StringOperationsImpl getStringOperations() {
        return this.stringOperations;
    }

    /**
     * The BytesImpl object to access its operations.
     */
    private final BytesImpl bytes;

    /**
     * Gets the BytesImpl object to access its operations.
     * 
     * @return the BytesImpl object.
     */
    public BytesImpl getBytes() {
        return this.bytes;
    }

    /**
     * The DatetimeOperationsImpl object to access its operations.
     */
    private final DatetimeOperationsImpl datetimeOperations;

    /**
     * Gets the DatetimeOperationsImpl object to access its operations.
     * 
     * @return the DatetimeOperationsImpl object.
     */
    public DatetimeOperationsImpl getDatetimeOperations() {
        return this.datetimeOperations;
    }

    /**
     * The DurationOperationsImpl object to access its operations.
     */
    private final DurationOperationsImpl durationOperations;

    /**
     * Gets the DurationOperationsImpl object to access its operations.
     * 
     * @return the DurationOperationsImpl object.
     */
    public DurationOperationsImpl getDurationOperations() {
        return this.durationOperations;
    }

    /**
     * The PlainDatesImpl object to access its operations.
     */
    private final PlainDatesImpl plainDates;

    /**
     * Gets the PlainDatesImpl object to access its operations.
     * 
     * @return the PlainDatesImpl object.
     */
    public PlainDatesImpl getPlainDates() {
        return this.plainDates;
    }

    /**
     * The PlainTimesImpl object to access its operations.
     */
    private final PlainTimesImpl plainTimes;

    /**
     * Gets the PlainTimesImpl object to access its operations.
     * 
     * @return the PlainTimesImpl object.
     */
    public PlainTimesImpl getPlainTimes() {
        return this.plainTimes;
    }

    /**
     * The CollectionsBytesImpl object to access its operations.
     */
    private final CollectionsBytesImpl collectionsBytes;

    /**
     * Gets the CollectionsBytesImpl object to access its operations.
     * 
     * @return the CollectionsBytesImpl object.
     */
    public CollectionsBytesImpl getCollectionsBytes() {
        return this.collectionsBytes;
    }

    /**
     * The CollectionsModelsImpl object to access its operations.
     */
    private final CollectionsModelsImpl collectionsModels;

    /**
     * Gets the CollectionsModelsImpl object to access its operations.
     * 
     * @return the CollectionsModelsImpl object.
     */
    public CollectionsModelsImpl getCollectionsModels() {
        return this.collectionsModels;
    }

    /**
     * The StringLiteralsImpl object to access its operations.
     */
    private final StringLiteralsImpl stringLiterals;

    /**
     * Gets the StringLiteralsImpl object to access its operations.
     * 
     * @return the StringLiteralsImpl object.
     */
    public StringLiteralsImpl getStringLiterals() {
        return this.stringLiterals;
    }

    /**
     * The IntLiteralsImpl object to access its operations.
     */
    private final IntLiteralsImpl intLiterals;

    /**
     * Gets the IntLiteralsImpl object to access its operations.
     * 
     * @return the IntLiteralsImpl object.
     */
    public IntLiteralsImpl getIntLiterals() {
        return this.intLiterals;
    }

    /**
     * The FloatLiteralsImpl object to access its operations.
     */
    private final FloatLiteralsImpl floatLiterals;

    /**
     * Gets the FloatLiteralsImpl object to access its operations.
     * 
     * @return the FloatLiteralsImpl object.
     */
    public FloatLiteralsImpl getFloatLiterals() {
        return this.floatLiterals;
    }

    /**
     * The BooleanLiteralsImpl object to access its operations.
     */
    private final BooleanLiteralsImpl booleanLiterals;

    /**
     * Gets the BooleanLiteralsImpl object to access its operations.
     * 
     * @return the BooleanLiteralsImpl object.
     */
    public BooleanLiteralsImpl getBooleanLiterals() {
        return this.booleanLiterals;
    }

    /**
     * The UnionStringLiteralsImpl object to access its operations.
     */
    private final UnionStringLiteralsImpl unionStringLiterals;

    /**
     * Gets the UnionStringLiteralsImpl object to access its operations.
     * 
     * @return the UnionStringLiteralsImpl object.
     */
    public UnionStringLiteralsImpl getUnionStringLiterals() {
        return this.unionStringLiterals;
    }

    /**
     * The UnionIntLiteralsImpl object to access its operations.
     */
    private final UnionIntLiteralsImpl unionIntLiterals;

    /**
     * Gets the UnionIntLiteralsImpl object to access its operations.
     * 
     * @return the UnionIntLiteralsImpl object.
     */
    public UnionIntLiteralsImpl getUnionIntLiterals() {
        return this.unionIntLiterals;
    }

    /**
     * The UnionFloatLiteralsImpl object to access its operations.
     */
    private final UnionFloatLiteralsImpl unionFloatLiterals;

    /**
     * Gets the UnionFloatLiteralsImpl object to access its operations.
     * 
     * @return the UnionFloatLiteralsImpl object.
     */
    public UnionFloatLiteralsImpl getUnionFloatLiterals() {
        return this.unionFloatLiterals;
    }

    /**
     * The RequiredAndOptionalsImpl object to access its operations.
     */
    private final RequiredAndOptionalsImpl requiredAndOptionals;

    /**
     * Gets the RequiredAndOptionalsImpl object to access its operations.
     * 
     * @return the RequiredAndOptionalsImpl object.
     */
    public RequiredAndOptionalsImpl getRequiredAndOptionals() {
        return this.requiredAndOptionals;
    }

    /**
     * Initializes an instance of OptionalClient client.
     * 
     * @param httpPipeline The HTTP pipeline to send requests through.
     * @param instrumentation The instance of instrumentation to report telemetry.
     * @param endpoint Service host.
     */
    public OptionalClientImpl(HttpPipeline httpPipeline, Instrumentation instrumentation, String endpoint) {
        this.httpPipeline = httpPipeline;
        this.instrumentation = instrumentation;
        this.endpoint = endpoint;
        this.stringOperations = new StringOperationsImpl(this);
        this.bytes = new BytesImpl(this);
        this.datetimeOperations = new DatetimeOperationsImpl(this);
        this.durationOperations = new DurationOperationsImpl(this);
        this.plainDates = new PlainDatesImpl(this);
        this.plainTimes = new PlainTimesImpl(this);
        this.collectionsBytes = new CollectionsBytesImpl(this);
        this.collectionsModels = new CollectionsModelsImpl(this);
        this.stringLiterals = new StringLiteralsImpl(this);
        this.intLiterals = new IntLiteralsImpl(this);
        this.floatLiterals = new FloatLiteralsImpl(this);
        this.booleanLiterals = new BooleanLiteralsImpl(this);
        this.unionStringLiterals = new UnionStringLiteralsImpl(this);
        this.unionIntLiterals = new UnionIntLiteralsImpl(this);
        this.unionFloatLiterals = new UnionFloatLiteralsImpl(this);
        this.requiredAndOptionals = new RequiredAndOptionalsImpl(this);
    }
}
