namespace java org.bdgenomics.services.reads

struct Alignment {
    1: required string referenceName;
    2: required i64 start;
    3: required i64 length;
    4: required string cigar;
}

struct Read {
    1: required string sequence;
    2: required string qual;
}

struct Record {
    1: required string sampleId;
    /**
     * This is data about where the read was aligned. If the read was not aligned, then this field will not be set.
     **/
    2: optional Alignment alignment;
    3: optional Read read;
}

struct SamplesResult {
    1: optional i32 next_start;
    2: required list<string> samples;
}

struct RangeQuery {
    1: required string referenceName;
    2: required i64 start;
    3: required i64 stop;
}

struct ReadsQuery {
    1: required list<string> samples;
    2: optional RangeQuery range;
}

exception InvalidRequestException {
    1: required string why;
}

exception PermissionDeniedException {
    1: required string why;
}

exception UnavailableException {
    1: optional string why;
}

service ReadService {

    SamplesResult retrieve_samples(
        1: i32 start,
        2: i32 count)
        throws (
            1: InvalidRequestException ire,
            2: PermissionDeniedException pde,
            3: UnavailableException ue);

    /**
     * This registers a request and returns a unique identifier used to page through requests.
     **/
    binary register_reads_query(
        1: ReadsQuery query
        ) throws (
            1: InvalidRequestException ire,
            2: PermissionDeniedException pde,
            3: UnavailableException ue);

    /**
     * This retrieves results from the service.
     **/
    list<Record> retrieve_reads(
        1: binary requestId,
        2: i32 count)
        throws (
            1: InvalidRequestException ire,
            2: PermissionDeniedException pde,
            3: UnavailableException ue);

    /**
     * This caches a result for the user.
     **/
    void cache_sample(
        1: string sampleId)
        throws (
            1: InvalidRequestException ire,
            2: PermissionDeniedException pde,
            3: UnavailableException ue);
}