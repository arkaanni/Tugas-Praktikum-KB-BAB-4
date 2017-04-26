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
import java.util.List;

public class MainColoring {

    /**
     * The problem we are solving.
     */
    public MapColoring mapColoring;

    /**
     * The initial assignment state.
     */
    public Assignment initial = Assignment.blank();

    /**
     * Setup from an input file.
     */
    public MainColoring(int banyakWarna) {
        initial = Assignment.blank();
        mapColoring = new MapColoring(banyakWarna);
    }

    public static void main(String[] args) {
        ArrayList<String> listWarna = new ArrayList<String>();
        listWarna.add("Merah");
        listWarna.add("Kuning");
        listWarna.add("Hijau");

        MainColoring m = new MainColoring(listWarna.size());

        Assignment solution = m.solve();

        if (solution == null) {
            System.out.println("Gak Ketemu");
            System.exit(1);
        }

        System.out.println("Ketemu");
        List<Variable> vars = m.mapColoring.variables();
        for (Variable v : vars) {
            System.out.println(v.description() + "="
                    + listWarna.get(((Integer) solution.getValue(v)) - 1));
        }

    }

    public Assignment solve() {
        MRVBacktrack solve = new MRVBacktrack(mapColoring, initial);
        return solve.solve();
    }
}
