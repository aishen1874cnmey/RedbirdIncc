//
// Copyright (c) 2019 Intel Corporation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

def isReleaseStream(branchName = env.GIT_BRANCH) {
    // what defines a main release branch
    def releaseStreams = [/.*master/, /.*california/, /.*delhi/, /.*edinburgh/]

    (branchName && (releaseStreams.collect { branchName =~ it ? true : false }).contains(true))
}

def didChange(expression, previous=env.GIT_PREVIOUS_SUCCESSFUL_COMMIT) {
    // If there was no previous successful build (as in building for first time) will return true.
    def diffCount = 0

    // if there is previous commit, then lets calculate the git diff
    if (previous != null) {
        diffCount = sh (
          returnStdout: true,
          script: "git diff --name-only ${env.GIT_COMMIT} ${previous} | grep \"${expression}\" | wc -l"
        ).trim().toInteger()

        // If the build is triggered manually
        if (previous == env.GIT_COMMIT) {
            diffCount = 1
        }
    } else {
        // if no previous commit, then this is probably the first build
        diffCount = 1
    }

    return diffCount > 0
}