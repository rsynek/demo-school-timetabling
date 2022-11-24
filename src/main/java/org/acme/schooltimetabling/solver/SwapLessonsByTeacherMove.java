/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.acme.schooltimetabling.solver;

import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.TimeTable;
import org.acme.schooltimetabling.domain.Timeslot;
import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;

public class SwapLessonsByTeacherMove extends AbstractMove<TimeTable> {

    private final Lesson leftLesson;
    private final Lesson rightLesson;

    public SwapLessonsByTeacherMove(Lesson leftLesson, Lesson rightLesson) {
        this.leftLesson = leftLesson;
        this.rightLesson = rightLesson;
    }

    @Override
    protected AbstractMove<TimeTable> createUndoMove(ScoreDirector<TimeTable> scoreDirector) {
        return new SwapLessonsByTeacherMove(leftLesson, leftLesson);
    }

    @Override
    protected void doMoveOnGenuineVariables(ScoreDirector<TimeTable> scoreDirector) {
        Timeslot leftTimeslot = leftLesson.getTimeslot();
        setLessonTimeslot(scoreDirector, leftLesson, rightLesson.getTimeslot());
        setLessonTimeslot(scoreDirector, rightLesson, leftTimeslot);
    }

    private void setLessonTimeslot(ScoreDirector<TimeTable> scoreDirector, Lesson lesson, Timeslot timeslot) {
        final String propertyName = "timeslot";
        scoreDirector.beforeVariableChanged(lesson, propertyName);
        lesson.setTimeslot(timeslot);
        scoreDirector.afterVariableChanged(lesson, propertyName);
    }

    @Override
    public boolean isMoveDoable(ScoreDirector<TimeTable> scoreDirector) {
        return leftLesson.getTeacher() == rightLesson.getTeacher()
                && leftLesson.getTimeslot() != rightLesson.getTimeslot();
    }

    @Override
    public String toString() {
        return "SwapLessonsByTeacherMove (" +
                "teacher=" + leftLesson.getTeacher() +
                ", leftLesson=" + leftLesson +
                ", rightLesson=" + rightLesson +
                ")";
    }
}
