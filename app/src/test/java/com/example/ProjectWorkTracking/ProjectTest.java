package com.example.ProjectWorkTracking;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests verifying the encapsulation, bounds-checking, and behavior of the Project model.
 */
public class ProjectTest {

    @Test
    public void defaultConstructor_initializesValidDefaults() {
        Project project = new Project();
        assertEquals("", project.getName());
        assertEquals("", project.getDescription());
        assertEquals(0, project.getBudget());
        assertEquals(0, project.getBudgetUsed());
        assertEquals(Project.PRIORITY_LOW, project.getPriority());
        assertFalse(project.isStatusActive());
        assertNotNull(project.getTeams());
        assertEquals(Project.NUM_TEAMS, project.getTeams().length);
        assertNotNull(project.getProgress());
        assertEquals(Project.NUM_PHASES, project.getProgress().length);
    }

    @Test
    public void parameterizedConstructor_clampsNegativeBudgetAndProgress() {
        Project project = new Project(
                "Alpha", "Desc", "01/01/2026", "01/02/2026", "Manager",
                -500, -100, Project.PRIORITY_HIGH,
                new Boolean[]{true, false, true}, true,
                new int[]{-20, 150, 50}
        );

        assertEquals(0, project.getBudget());
        assertEquals(0, project.getBudgetUsed());
        assertEquals(0, project.getPhaseProgress(Project.PHASE_DESIGN));
        assertEquals(100, project.getPhaseProgress(Project.PHASE_DEVELOPMENT));
        assertEquals(50, project.getPhaseProgress(Project.PHASE_TESTING));
    }

    @Test
    public void progressIncrementAndDecrement_respectsBounds() {
        Project project = new Project();
        project.setPhaseProgress(Project.PHASE_DESIGN, 95);

        project.incrementPhaseProgress(Project.PHASE_DESIGN);
        assertEquals(100, project.getPhaseProgress(Project.PHASE_DESIGN));

        project.incrementPhaseProgress(Project.PHASE_DESIGN);
        assertEquals(100, project.getPhaseProgress(Project.PHASE_DESIGN));

        project.setPhaseProgress(Project.PHASE_DESIGN, 5);
        project.decrementPhaseProgress(Project.PHASE_DESIGN);
        assertEquals(0, project.getPhaseProgress(Project.PHASE_DESIGN));

        project.decrementPhaseProgress(Project.PHASE_DESIGN);
        assertEquals(0, project.getPhaseProgress(Project.PHASE_DESIGN));
    }

    @Test
    public void teamAssignment_worksCorrectly() {
        Project project = new Project();
        assertFalse(project.isTeamAssigned(Project.TEAM_TECH));

        project.setTeamAssigned(Project.TEAM_TECH, true);
        assertTrue(project.isTeamAssigned(Project.TEAM_TECH));
        assertFalse(project.isTeamAssigned(Project.TEAM_FINANCE));

        // Invalid index should not throw
        assertFalse(project.isTeamAssigned(99));
        project.setTeamAssigned(99, true);
    }

    @Test
    public void equalityAndHashCode_workProperly() {
        Project p1 = new Project("P1", "Desc", "01/01/2026", "01/02/2026", "Alice", 1000, 1, new Boolean[]{true, false, false}, true, new int[]{10, 20, 30});
        Project p2 = new Project("P1", "Desc", "01/01/2026", "01/02/2026", "Alice", 1000, 1, new Boolean[]{true, false, false}, true, new int[]{10, 20, 30});
        Project p3 = new Project("P3", "Desc", "01/01/2026", "01/02/2026", "Alice", 1000, 1, new Boolean[]{true, false, false}, true, new int[]{10, 20, 30});

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1, p3);
    }
}
