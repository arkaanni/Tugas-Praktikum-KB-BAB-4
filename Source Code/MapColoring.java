/*
 * Copyright (C) 2017 abdanmulia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MapColoring extends CSPProblem {

    /**
     * The size of the board.
     */
    int boardSize = 8;

    // List of variable tiles
    List<Variable> tiles;

    // List of all constraints
    List<Constraint> constraints;

    // List of tile domain
    List<Object> domain = new LinkedList<Object>();

    List<String> warna = new ArrayList<String>();

    public MapColoring(int size) {
        // Generate everything
        boardSize = size;
        for (int i = 1; i <= boardSize; i++) {
            domain.add(i);
        }
        tiles = new ArrayList<Variable>();
        constraints = new ArrayList<Constraint>();
        variables();
        constraints();
    }

    /**
     * Returns a list of variables associated with the problem.
     */
    public List<Variable> variables() {
        if (tiles.size() == 0) {
            tiles.add(new MapTile(0, "Aceh"));
            tiles.add(new MapTile(1, "Sumatera Utara"));
            tiles.add(new MapTile(2, "Riau"));
            tiles.add(new MapTile(3, "Sumatera Barat"));
            tiles.add(new MapTile(4, "Jambi"));
            tiles.add(new MapTile(5, "Bengkulu"));
            tiles.add(new MapTile(6, "Sumatera Selatan"));
            tiles.add(new MapTile(7, "Lampung"));
        }
        return tiles;
    }

    /**
     * Returns a list of constraints associated with the problem.
     */
    public List<Constraint> constraints() {
        if (constraints.size() == 0) {
            // Add the row constraints
            AllDiff constr;

            //ACEH->Sumatera Utara
            constr = new AllDiff();
            constr.variables.add(tiles.get(0));
            constr.variables.add(tiles.get(1));
            constraints.add(constr);

            //Sumatera Utara->Riau
            constr = new AllDiff();
            constr.variables.add(tiles.get(1));
            constr.variables.add(tiles.get(2));
            constraints.add(constr);

            //Sumatera Utara->Sumatera Barat
            constr = new AllDiff();
            constr.variables.add(tiles.get(1));
            constr.variables.add(tiles.get(3));
            constraints.add(constr);

            //Riau->Sumatera Barat
            constr = new AllDiff();
            constr.variables.add(tiles.get(2));
            constr.variables.add(tiles.get(3));
            constraints.add(constr);

            //Riau->Jambi
            constr = new AllDiff();
            constr.variables.add(tiles.get(2));
            constr.variables.add(tiles.get(4));
            constraints.add(constr);

            //Sumatera Barat->Jambi
            constr = new AllDiff();
            constr.variables.add(tiles.get(3));
            constr.variables.add(tiles.get(4));
            constraints.add(constr);

            //Sumatera Barat->Bengkulu
            constr = new AllDiff();
            constr.variables.add(tiles.get(3));
            constr.variables.add(tiles.get(5));
            constraints.add(constr);

            //Jambi->Sumatera Selatan
            constr = new AllDiff();
            constr.variables.add(tiles.get(4));
            constr.variables.add(tiles.get(6));
            constraints.add(constr);

            //Bengkulu->Sumatera Selatan
            constr = new AllDiff();
            constr.variables.add(tiles.get(5));
            constr.variables.add(tiles.get(6));
            constraints.add(constr);

            //Sumatera Selatan->Lampung
            constr = new AllDiff();
            constr.variables.add(tiles.get(6));
            constr.variables.add(tiles.get(7));
            constraints.add(constr);

        }
        return constraints;
    }

    /**
     * Implements each tile as a variable in a CSP
     */
    public class MapTile extends Variable {

        public int tileNumber;
        public String nama;

        public MapTile(int i, String nama) {
            tileNumber = i;
            this.nama = nama;
        }

        public String description() {
            return nama;
        }

        public List<Object> domain() {
            return domain;
        }
    }

    /**
     * Simple constraint that requires all variables to have a unique value.
     */
    public class AllDiff extends Constraint {

        public List<Variable> variables = new LinkedList<Variable>();

        public boolean satisfied(Assignment asign) {
            boolean[] seen = new boolean[boardSize + 1];
            for (Variable v : variables) {
                Integer val = (Integer) asign.getValue(v);
                if (val == null || seen[val]) {
                    return false;
                }
                seen[val] = true;
            }
            return true;
        }

        public boolean consistent(Assignment asign) {
            boolean[] seen = new boolean[boardSize + 1];
            boolean[] avail = new boolean[boardSize + 1];
            int constraintDomain = 0;

            for (Variable v : variables) {
                // Check if this variable adds to the domain of the constraint
                for (Object val : domainValues(asign, v)) {
                    if (!avail[(Integer) val]) {
                        constraintDomain++;
                        avail[(Integer) val] = true;
                    }
                }

                // Check for a duplicate value
                Integer val = (Integer) asign.getValue(v);
                if (val != null) {
                    if (seen[val]) {
                        return false;
                    }
                    seen[val] = true;
                }
            }

            // Check if there are not enough values
            if (variables.size() > constraintDomain) {
                return false;
            }
            return true;
        }

        public List<Variable> reliesOn() {
            return variables;
        }

        public String description() {
            String desc = "Constraint on {\n";
            for (Variable v : variables) {
                desc += "\t" + v.description() + "\n";
            }
            desc += "}";
            return desc;
        }
    }
}
