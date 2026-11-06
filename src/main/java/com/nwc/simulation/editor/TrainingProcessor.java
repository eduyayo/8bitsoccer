package com.nwc.simulation.editor;

import com.nwc.simulation.config.PlayerProfile;

public class TrainingProcessor {
    public void processDailyTraining(PlayerProfile profile, TrainingRegimen regimen) {
        // 1. Apply primary training gains
        profile.physical.acceleration += regimen.getPhysicalIntensity() * 0.1f;
        profile.technical.finishing += regimen.isTechnicalFocus() ? 1 : 0;

        // 2. Inter-property side effects
        profile.physical.fatigue += regimen.getPhysicalIntensity() * 1.5f;

        if (profile.physical.fatigue > 70.0f) {
            // High fatigue stunts technical growth
            profile.technical.finishing = Math.max(0, profile.technical.finishing - 1);
        }

        if (profile.physical.fatigue > 80.0f) {
            profile.hidden.daysFatigued++;
            if (profile.hidden.daysFatigued >= 3) {
                profile.hidden.injuryProneness *= 2.5f;
            }
        } else {
            profile.hidden.daysFatigued = 0;
        }

        // 3. Mental-performance loop
        if (regimen.isIdenticalToPreviousDays(5)) {
            profile.mental.morale = Math.max(0f, profile.mental.morale - 5.0f);
        } else if (profile.physical.fatigue > 85.0f) {
            profile.mental.morale = Math.max(0f, profile.mental.morale - 5.0f);
        }

        if (regimen.isBenched()) {
            profile.hidden.matchSharpness = Math.max(0f, profile.hidden.matchSharpness - 5.0f);
            float moraleDrop = 2.5f;
            if (profile.mental.composure < 50.0f || profile.hidden.volatileTrait) {
                moraleDrop *= 2.0f;
            }
            profile.mental.morale = Math.max(0f, profile.mental.morale - moraleDrop);
        }
    }
}
