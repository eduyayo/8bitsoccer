package com.nwc.simulation.editor;

import com.nwc.simulation.config.PlayerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingProcessorTest {

    private TrainingProcessor processor;
    private PlayerProfile profile;

    @BeforeEach
    void setUp() {
        processor = new TrainingProcessor();
        profile = PlayerProfile.createDefault(10);
        profile.hidden.injuryProneness = 1.0f;
        profile.mental.morale = 50.0f;
        profile.hidden.matchSharpness = 50.0f;
    }

    @Test
    void testFatiguePenalty() {
        // Force fatigue above 70
        profile.physical.fatigue = 65.0f;

        // Initial finishing is 5 from createDefault
        TrainingRegimen regimen = new TrainingRegimen(10.0f, true, 1, false);

        // physical intensity 10 -> fatigue +15 = 80 > 70
        // technical focus true -> finishing +1 = 6
        // fatigue penalty -> finishing -1 = 5
        processor.processDailyTraining(profile, regimen);

        assertEquals(80.0f, profile.physical.fatigue, 0.01f);
        assertEquals(5.0f, profile.technical.finishing, 0.01f);
    }

    @Test
    void testInjuryRiskMultiplication() {
        profile.physical.fatigue = 90.0f;

        // Day 1
        processor.processDailyTraining(profile, new TrainingRegimen(0f, false, 1, false));
        assertEquals(1, profile.hidden.daysFatigued);
        assertEquals(1.0f, profile.hidden.injuryProneness, 0.01f);

        // Day 2
        processor.processDailyTraining(profile, new TrainingRegimen(0f, false, 2, false));
        assertEquals(2, profile.hidden.daysFatigued);
        assertEquals(1.0f, profile.hidden.injuryProneness, 0.01f);

        // Day 3
        processor.processDailyTraining(profile, new TrainingRegimen(0f, false, 3, false));
        assertEquals(3, profile.hidden.daysFatigued);
        assertEquals(2.5f, profile.hidden.injuryProneness, 0.01f);
    }

    @Test
    void testMoraleDecay() {
        // Morale drops on identical days >= 5 or fatigue > 85
        profile.mental.morale = 50.0f;

        // Identical days >= 5
        processor.processDailyTraining(profile, new TrainingRegimen(0f, false, 5, false));
        assertEquals(45.0f, profile.mental.morale, 0.01f);

        // Fatigue > 85
        profile.physical.fatigue = 90.0f;
        processor.processDailyTraining(profile, new TrainingRegimen(0f, false, 1, false));
        assertEquals(40.0f, profile.mental.morale, 0.01f);

        // Benched decay
        profile.physical.fatigue = 0.0f;
        profile.mental.morale = 40.0f;
        profile.mental.composure = 60.0f;
        profile.hidden.volatileTrait = false;
        profile.hidden.matchSharpness = 50.0f;
        processor.processDailyTraining(profile, new TrainingRegimen(0f, false, 1, true));

        assertEquals(45.0f, profile.hidden.matchSharpness, 0.01f);
        // Base bench drop is 2.5, morale drops by 2.5
        assertEquals(37.5f, profile.mental.morale, 0.01f);

        // Benched decay (volatile trait)
        // Set fatigue to 0 to avoid morale drop from fatigue > 85
        profile.physical.fatigue = 0.0f;
        profile.mental.morale = 37.5f;
        profile.hidden.volatileTrait = true;
        processor.processDailyTraining(profile, new TrainingRegimen(0f, false, 1, true));
        // Volatile -> bench drop is 5.0
        assertEquals(32.5f, profile.mental.morale, 0.01f);
    }
}
