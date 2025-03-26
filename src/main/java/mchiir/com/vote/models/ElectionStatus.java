package mchiir.com.vote.models;

/**
 * Represents the status of an election:
 * UPCOMING - Not yet started,
 * ONGOING - Voting is in progress,
 * CLOSED - Voting has ended.
 */
public enum ElectionStatus {
    UPCOMING,
    ONGOING,
    CLOSED
}
