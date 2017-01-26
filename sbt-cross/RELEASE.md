# Releases

## Versioning scheme

Versions are `<major>.<minor>`.

* Each major version series has a branch, with a SNAPSHOT version published for the latest.

* Each major.minor version has a tag, which triggers publishing a release version.

Commits are typically developed for `master` and then cherry-picked to branches as needed.

```
A - B - C - D - E - F - G - H (master)
  \          \
   C'         E' - G' - H' (2)
    \       (2.0) (2.1)
     D'
   (1.0, 1)

Published versions:
  1.0
  1-SNAPSHOT
  2.0
  2.1
  2-SNAPSHOT
  master-SNAPSHOT
```

## Release process

### 1. Create and checkout branches

New major version: create, checkout, and push a new branch for the major series.

```sh
git checkout -b <major> master
git push -u HEAD
```

New minor version: checkout major series branch

```sh
git checkout <major>
```

### 2. Tag

Push a tag with the new version.

```sh
git push HEAD:refs/tags/<major>.<minor>
```

This triggers a release in Github and Sonatype.

### 3. Update documentation

Update documentation and examples on `master` to point to the new version, commit, and push.
