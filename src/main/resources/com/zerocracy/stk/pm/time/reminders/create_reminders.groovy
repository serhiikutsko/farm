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
package com.zerocracy.stk.pm.time.reminders

import com.jcabi.xml.XML
import com.zerocracy.farm.Assume
import com.zerocracy.jstk.Project
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pm.ClaimOut
import com.zerocracy.pm.in.Orders
import com.zerocracy.pm.time.Reminders
import java.time.ZoneOffset
import java.time.ZonedDateTime
/**
 * @todo #266:30min Notify users about job in github
 *  with reminder's label. It can be new stakeholder which will
 *  react to 'Ping' claim.
 */
def exec(Project project, XML xml) {
  new Assume(project, xml).type('Ping')
  new Assume(project, xml).notPmo()
  def claim = new ClaimIn(xml)
  def reminders = new Reminders(project).bootstrap()
  def orders = new Orders(project).bootstrap()
  def expired = orders.olderThan(
    ZonedDateTime.ofInstant(
      claim.created().toInstant(), ZoneOffset.UTC
    ).minusDays(5)
  )
  for (String job : expired) {
    String label = '5 days'
    String login = orders.performer(job)
    if (reminders.add(job, login, label)) {
      new ClaimOut()
        .type('New reminder posted')
        .param('job', job)
        .param('label', label)
        .param('login', login)
        .postTo(project)
    }
  }
}