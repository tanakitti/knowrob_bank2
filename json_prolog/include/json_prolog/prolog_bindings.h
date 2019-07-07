/* 
 * Copyright (c) 2010, Lorenz Moesenlechner <moesenle@cs.tum.edu>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Willow Garage, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

#ifndef __JSON_PROLOG_BINDINGS_H__
#define __JSON_PROLOG_BINDINGS_H__

#include <string>
#include <map>
#include <vector>
#include <stdexcept>

#include <json_prolog/prolog_value.h>

namespace json_prolog
{
  
class PrologBindings
{
public:
  class VariableUnbound
    : public std::runtime_error
  {
  public:
    VariableUnbound(const std::string &var_name)
      : std::runtime_error(var_name) {}
  };

  class JSONParseError
    : public std::runtime_error
  {
  public:
    JSONParseError(const std::string &msg)
      : std::runtime_error(msg) {}
  };
  
  const PrologValue &operator[](const std::string &var_name) const;
  operator std::map<std::string, PrologValue>() const { return bdgs_; }

  std::map<std::string, PrologValue>::iterator begin() { return bdgs_.begin(); }
  std::map<std::string, PrologValue>::const_iterator begin() const { return bdgs_.begin(); }

  std::map<std::string, PrologValue>::iterator end() { return bdgs_.end(); }
  std::map<std::string, PrologValue>::const_iterator end() const { return bdgs_.end(); }

  static PrologBindings parseJSONBindings(const std::string &json_bdgs);
private:
  std::map<std::string, PrologValue> bdgs_;
};

}

#endif