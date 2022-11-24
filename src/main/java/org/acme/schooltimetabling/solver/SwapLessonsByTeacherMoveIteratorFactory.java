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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.acme.schooltimetabling.domain.Lesson;
import org.acme.schooltimetabling.domain.TimeTable;
import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveIteratorFactory;

public class SwapLessonsByTeacherMoveIteratorFactory implements MoveIteratorFactory<TimeTable, SwapLessonsByTeacherMove> {

    @Override
    public long getSize(ScoreDirector<TimeTable> scoreDirector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<SwapLessonsByTeacherMove> createOriginalMoveIterator(ScoreDirector<TimeTable> scoreDirector) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<SwapLessonsByTeacherMove> createRandomMoveIterator(ScoreDirector<TimeTable> scoreDirector, Random workingRandom) {
        Map<String, List<Lesson>> lessonsByTeacher = scoreDirector.getWorkingSolution().getLessonList().stream()
                .filter(lesson -> lesson.getTimeslot() != null)
                .collect(Collectors.groupingBy(Lesson::getTeacher));
        return new SwapLessonsByTeacherMoveIterator(lessonsByTeacher, workingRandom);
    }

    private static class SwapLessonsByTeacherMoveIterator implements Iterator<SwapLessonsByTeacherMove> {

        private final Map<String, List<Lesson>> lessonsByTeacher;
        private final String [] teachers;
        private final Random workingRandom;

        public SwapLessonsByTeacherMoveIterator(Map<String, List<Lesson>> lessonsByTeacher, Random workingRandom) {
            this.lessonsByTeacher = lessonsByTeacher;
            this.workingRandom = workingRandom;
            this.teachers = lessonsByTeacher.keySet().toArray(new String[0]);
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public SwapLessonsByTeacherMove next() {
             String teacher = teachers[workingRandom.nextInt(teachers.length)];
             List<Lesson> lessons = lessonsByTeacher.get(teacher);
             Lesson leftLesson = lessons.get(workingRandom.nextInt(lessons.size()));
             Lesson rightLesson = lessons.get(workingRandom.nextInt(lessons.size()));
             return new SwapLessonsByTeacherMove(leftLesson, rightLesson);
        }
    }

}
