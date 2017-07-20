/**
 * Copyright (c) 2016-2017 Zerocracy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to read
 * the Software only. Permissions is hereby NOT GRANTED to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.zerocracy.pmo;

import com.zerocracy.Xocument;
import com.zerocracy.jstk.Item;
import com.zerocracy.jstk.Project;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.xembly.Directives;

/**
 * Awards of one person.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @since 0.12
 */
public final class Awards {

    /**
     * Project.
     */
    private final Project project;

    /**
     * Login of the person.
     */
    private final String login;

    /**
     * Ctor.
     * @param pkt Project
     * @param user The user
     */
    public Awards(final Project pkt, final String user) {
        this.project = pkt;
        this.login = user;
    }

    /**
     * Bootstrap it.
     * @return This
     * @throws IOException If fails
     */
    public Awards bootstrap() throws IOException {
        try (final Item item = this.item()) {
            new Xocument(item.path()).bootstrap("pmo/awards");
        }
        return this;
    }

    /**
     * Add points to the list.
     * @param points How many points
     * @param job Job ID
     * @param reason The reason
     * @throws IOException If fails
     */
    public void add(final int points, final String job, final String reason)
        throws IOException {
        try (final Item item = this.item()) {
            new Xocument(item.path()).modify(
                new Directives()
                    .xpath("/awards")
                    .add("award")
                    .add("points")
                    .set(points)
                    .up()
                    .add("added")
                    .set(
                        ZonedDateTime.now().format(
                            DateTimeFormatter.ISO_INSTANT
                        )
                    )
                    .up()
                    .add("project")
                    .set(this.project.toString())
                    .up()
                    .add("reason")
                    .set(reason)
                    .up()
                    .add("job")
                    .set(job)
            );
        }
    }

    /**
     * Total points.
     * @return Points
     * @throws IOException If fails
     */
    public int total() throws IOException {
        try (final Item item = this.item()) {
            return Integer.parseInt(
                new Xocument(item.path()).xpath(
                    "sum(/awards/award/points/text())"
                ).get(0)
            );
        }
    }

    /**
     * The item.
     * @return Item
     * @throws IOException If fails
     */
    private Item item() throws IOException {
        return this.project.acq(
            String.format("awards/%s.xml", this.login)
        );
    }

}