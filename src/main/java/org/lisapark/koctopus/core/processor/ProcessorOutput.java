/* 
 * Copyright (C) 2013 Lisa Park, Inc. (www.lisa-park.net)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lisapark.koctopus.core.processor;

import org.lisapark.koctopus.core.Output;
import org.lisapark.koctopus.core.Persistable;
import org.lisapark.koctopus.core.ValidationException;
import org.lisapark.koctopus.core.event.Attribute;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author dave sinclair(david.sinclair@lisa-park.com)
 */
@Persistable
public class ProcessorOutput extends Output {

    private final Attribute attribute;

    protected ProcessorOutput(ProcessorOutput existingOutput) {
        super(existingOutput);

        // todo - getting a little hairy with the copying
        // when we call the super class copy constructor, it makes a deep copy of the event type which
        // also holds the attribute we are interested in. Need to get this NEW copy, now create one ourselves
        this.attribute = getEventType().getAttributeByName(existingOutput.attribute.getName());
    }

    public ProcessorOutput(Builder builder) {
        super(builder.id, builder.name, builder.description);
        this.attribute = builder.attribute;

        // add the output to the event
        addAttribute(builder.attribute);
    }

    public Class<?> getAttributeType() {
        return attribute.getType();
    }

    public void setAttributeName(String name) throws ValidationException {
        attribute.setName(name);
    }

    public String getAttributeName() {
        return attribute.getName();
    }

    @Override
    public ProcessorOutput copyOf() {
        return new ProcessorOutput(this);
    }

    public static Builder<String> stringOutputWithId(int id) {
        return new Builder<>(id, String.class);
    }

    public static Builder<Boolean> booleanOutputWithId(int id) {
        return new Builder<>(id, Boolean.class);
    }

    public static Builder<Long> longOutputWithId(int id) {
        return new Builder<>(id, Long.class);
    }

    public static Builder<Short> shortOutputWithId(int id) {
        return new Builder<>(id, Short.class);
    }

    public static Builder<Integer> integerOutputWithId(int id) {
        return new Builder<>(id, Integer.class);
    }

    public static Builder<Double> doubleOutputWithId(int id) {
        return new Builder<>(id, Double.class);
    }

    public static Builder<Float> floatOutputWithId(int id) {
        return new Builder<>(id, Float.class);
    }

    public static class Builder<T> {
        private final int id;
        private String name;
        private String description;
        private final Class<T> type;
        private String attributeName;
        private Attribute attribute;

        private Builder(int id, Class<T> type) {
            this.id = id;
            this.type = type;
        }

        public Builder<T> name(String name) {
            this.name = name;
            return this;
        }

        public Builder<T> nameAndDescription(String name) {
            this.name = name;
            this.description = name;
            return this;
        }

        public Builder<T> attributeName(String attributeName) {
            this.attributeName = attributeName;
            return this;
        }

        public Builder<T> description(String description) {
            this.description = description;
            return this;
        }

        @SuppressWarnings("unchecked")
        public ProcessorOutput build() throws ValidationException {
            checkState(attributeName != null, "attributeName is required");
            attribute = Attribute.newAttribute(type, attributeName);
            return new ProcessorOutput(this);
        }
    }
}
