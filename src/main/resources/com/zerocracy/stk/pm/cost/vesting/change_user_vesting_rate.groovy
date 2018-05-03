/**
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
package com.zerocracy.stk.pm.cost.vesting

import com.jcabi.xml.XML
import com.zerocracy.Par
import com.zerocracy.Project
import com.zerocracy.cash.Cash
import com.zerocracy.farm.Assume
import com.zerocracy.pm.ClaimIn
import com.zerocracy.pm.cost.Vesting

def exec(Project project, XML xml) {
  new Assume(project, xml).notPmo()
  new Assume(project, xml).type('Change user vesting rate')
  new Assume(project, xml).roles('ARC', 'PO')
  ClaimIn claim = new ClaimIn(xml)
  String login = claim.param('login')
  Cash rate = new Cash.S(claim.param('rate'))
  Vesting vesting = new Vesting(project).bootstrap()
  vesting.rate(login, rate)
  claim.reply(
    new Par(
      'Vesting rate for @%s was set to %s, according to §37'
    ).say(login, rate)
  ).postTo(project)
}
