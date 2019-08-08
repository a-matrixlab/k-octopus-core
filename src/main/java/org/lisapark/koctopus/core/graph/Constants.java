/* 
 * Copyright (C) 2019 Lisa Park, Inc. (www.lisa-park.net)
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
package org.lisapark.koctopus.core.graph;

/**
 *
 * @author alexmylnikov
 */
public interface Constants {
    
    String OPERATOR = "operator";
    String INPUT = "input";
    String OUTPUT = "output";
    String OUTPUT_FILE = "outputfile";
    String INPUT_FILE = "inputfile";
    String PARAMS = "params";
    String COMMAND = "command";
    String END_OF_STREAM = "!@#$END$%^";
    String DEFAULT_TRANS_URL = "redis://localhost";
}
