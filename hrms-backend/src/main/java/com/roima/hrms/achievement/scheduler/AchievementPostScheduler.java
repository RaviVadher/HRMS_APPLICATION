package com.roima.hrms.achievement.scheduler;

import com.roima.hrms.achievement.entity.AchievementPost;
import com.roima.hrms.achievement.enums.PostType;
import com.roima.hrms.achievement.enums.PostVisibility;
import com.roima.hrms.achievement.repository.AchievementPostRepository;
import com.roima.hrms.user.entity.User;
import com.roima.hrms.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class AchievementPostScheduler {

    private final AchievementPostRepository achievementPostRepository;
    private final UserRepository userRepository;

    public AchievementPostScheduler(AchievementPostRepository achievementPostRepository,
                                    UserRepository userRepository) {
        this.achievementPostRepository = achievementPostRepository;
        this.userRepository = userRepository;
    }

    /**
     * Scheduled task to create birthday posts
     * Runs daily at 00:00 (midnight)
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void createBirthdayPosts() {
        log.info("Starting birthday post generation...");

        LocalDate today = LocalDate.now();
        List<User> birthdayEmployees = userRepository.findByBirthdayToday(today);
        for (User employee : birthdayEmployees) {
            // Check if birthday post already exists for today
            if (!achievementPostRepository.existsByAuthorAndPostTypeAndCreatedAtDate(
                    employee, PostType.BIRTHDAY, today)) {

                AchievementPost birthdayPost = new AchievementPost();
                birthdayPost.setTitle("Birthday Celebration");
                birthdayPost.setDescription("Today is " + employee.getName() + "'s birthday!");
                birthdayPost.setPostType(PostType.BIRTHDAY);
                birthdayPost.setAuthor(employee);
                birthdayPost.setIsSystemGenerated(true);
                birthdayPost.setVisibility(PostVisibility.ALL_EMPLOYEES);
                birthdayPost.setIsDeleted(false);
                birthdayPost.setTags("birthday,celebration");
                achievementPostRepository.save(birthdayPost);
                log.info("Birthday post created for employee: {}", employee.getName());
            }
        }
    }

    /**
     * Scheduled task to create work anniversary posts
     * Runs daily at 00:00 (midnight)
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void createWorkAnniversaryPosts() {
        log.info("Starting work anniversary post generation...");

        LocalDate today = LocalDate.now();
        List<User> anniversaryEmployees = userRepository.findByAnniversaryToday(today);
        for (User employee : anniversaryEmployees) {
            // Check if anniversary post already exists for today
            if (!achievementPostRepository.existsByAuthorAndPostTypeAndCreatedAtDate(
                    employee, PostType.WORK_ANNIVERSARY, today)) {

                int yearsCompleted = calculateYearsCompleted(employee.getJoining_date(), today);

                AchievementPost anniversaryPost = new AchievementPost();
                anniversaryPost.setTitle("Work Anniversary");
                anniversaryPost.setDescription(employee.getName() + " completes " + yearsCompleted +
                        " year" + (yearsCompleted > 1 ? "s" : "") + " at the organization! ");
                anniversaryPost.setPostType(PostType.WORK_ANNIVERSARY);
                anniversaryPost.setAuthor(employee);
                anniversaryPost.setIsSystemGenerated(true);
                anniversaryPost.setVisibility(PostVisibility.ALL_EMPLOYEES);
                anniversaryPost.setIsDeleted(false);
                anniversaryPost.setTags("anniversary,workanniversary");

                achievementPostRepository.save(anniversaryPost);
                log.info("Anniversary post created for employee: {}", employee.getName());
            }
        }
    }

    /**
     * Calculate years completed from joining date to today
     */
    private int calculateYearsCompleted(LocalDate joiningDate, LocalDate today) {
        return today.getYear() - joiningDate.getYear();
    }
}
