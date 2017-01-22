# Release process

## Major release

1. Edit `sbt-cross/version` file, bumping to the next major version.
2. Push master.
3. `git checkout -b <release_major_version>.x HEAD^`
4. Push the new major branch.
5. Switch GitHub to show new major branch by default.
6. Start at step 3 of minor release.

## Minor release

1. Edit `sbt-cross/version` file,  bumping to the next minor version.
2. Push the branch.
3. `git tag "v$(cat sbt-cross/version | xargs)"`
4. Push the new tag.
