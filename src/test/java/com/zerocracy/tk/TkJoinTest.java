/*
 * Copyright (c) 2016-2018 Zerocracy
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
package com.zerocracy.tk;

import com.jcabi.matchers.XhtmlMatchers;
import com.zerocracy.Farm;
import com.zerocracy.farm.fake.FkFarm;
import com.zerocracy.farm.props.PropsFarm;
import com.zerocracy.pmo.People;
import java.io.IOException;
import java.time.Instant;
import org.cactoos.iterable.IterableOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.text.StringContainsInOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.takes.rq.RqFake;
import org.takes.rs.RsPrint;

/**
 * Test case for {@link TkJoin}.
 *
 * @since 1.0
 * @todo #1568:30min TkJoinTest is not checking if join page is being rendered
 *  correctly; it just checks if something is rendered. Improve this test
 *  making sure that a user that not joined yet is requisting /join and that
 *  join page is bein correctly fetched.
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class TkJoinTest {

    @Test
    public void renderJoinPage() throws Exception {
        final Farm farm = new PropsFarm(new FkFarm());
        MatcherAssert.assertThat(
            XhtmlMatchers.xhtml(
                new RsPrint(
                    new TkApp(farm).act(
                        new RqWithUser.WithInit(
                            farm, new RqFake("GET", "/join")
                        )
                    )
                ).printBody()
            ),
            XhtmlMatchers.hasXPaths("//xhtml:body")
        );
    }

    /**
     * {@link TkJoinPost} can show resume if user already applied.
     * {@link TkJoinPost} must show user's resume, if there is one, when
     * user tries to access <code>/join</code> endpoint.
     * @throws IOException If something goes wrong accessing page
     */
    @Test
    @Ignore
    public void showsResumeIfAlreadyApplied() throws Exception {
        final Farm farm = new PropsFarm(new FkFarm());
        final People people = new People(farm).bootstrap();
        final String uid = "yegor256";
        people.touch(uid);
        people.apply(uid, Instant.now());
        MatcherAssert.assertThat(
            new RsPrint(
                new TkApp(farm).act(
                    new RqWithUser.WithInit(
                        farm, new RqFake("GET", "/join")
                    )
                )
            ).printBody(),
            new StringContainsInOrder(
                new IterableOf<>(
                    "User",
                    "here is your resume."
                )
            )
        );
    }
}
