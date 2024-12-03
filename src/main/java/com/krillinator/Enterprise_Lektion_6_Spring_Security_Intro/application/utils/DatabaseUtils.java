package com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils;

import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.application.utils.records.UserExistenceResult;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.CustomUser;
import com.krillinator.Enterprise_Lektion_6_Spring_Security_Intro.user.repository.UserRepository;

import java.util.Optional;


public class DatabaseUtils {

    public enum UserExistenceStatus {
        DUPLICATE_TASK_FOUND,
        BOTH_USERS_EXIST,
        OWNER_NOT_FOUND,
        PARTNER_NOT_FOUND,
        TASK_NOT_FOUND,
        ILLEGAL_OPERATION
    }

    /**
     * Checks if both the owner and the partner exist in the system.
     *
     * <p>This method queries the database to check if both the owner and partner users exist in the system.
     * It returns a {@code UserExistenceResult} object that contains:
     * <ul>
     *     <li>The {@code UserExistenceStatus} indicating the result of the check (e.g., whether both users exist,
     *         only the owner exists, or only the partner exists).</li>
     *     <li>The {@code CustomUser} objects for both the owner and the partner, if they exist.</li>
     * </ul>
     * If no user is found for either the owner or partner, the result contains null values for the respective
     * {@code CustomUser} objects.
     *
     * @param ownerUsername The username of the owner to search for.
     * @param partnerUsername The username of the partner to search for.
     * @param userRepository The repository used to search for users.
     * @return A {@code UserExistenceResult} containing the {@code UserExistenceStatus} and the {@code CustomUser} objects
     *         (or null if the user was not found).
     * @throws IllegalArgumentException if the provided usernames are null or empty.
     */
     public static UserExistenceResult checkUsersExistence(
             String ownerUsername,
             String partnerUsername,
             UserRepository userRepository
     )
     {
        if (ownerUsername == null || ownerUsername.isEmpty() || partnerUsername == null || partnerUsername.isEmpty()) {
            throw new IllegalArgumentException("Usernames cannot be null or empty.");
        }

        // Check if both users exist
        Optional<CustomUser> owner = userRepository.findByUsername(ownerUsername);
        Optional<CustomUser> partner = userRepository.findByUsername(partnerUsername);

        if (owner.isPresent() && partner.isPresent()) {
            return new UserExistenceResult(UserExistenceStatus.BOTH_USERS_EXIST, owner.get(), partner.get());
        } else if (owner.isEmpty()) {
            return new UserExistenceResult(UserExistenceStatus.OWNER_NOT_FOUND, null, null);
        } else {
            return new UserExistenceResult(UserExistenceStatus.PARTNER_NOT_FOUND, null, null);
        }
    }
}
