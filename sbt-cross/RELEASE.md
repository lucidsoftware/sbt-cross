# Release process

Versions are `<major>.<minor>`.

The latest major series is `master`.

Other major series are kept on maintenance branches.

```
A - B - C - D - E (master: 3.x)
  \      \
    C'    E'
 (1: 1.x) (2: 2.x)
```

## 1. Create and checkout branches

New major series: checkout master, and push a branch of the old major version.

```sh
git checkout master
git push HEAD:<old_major>
```

New version of latest series: checkout master

```sh
git checkout
```

New version of maintenance series: checkout series branch

```sh
git checkout <major>
```

## 2. Update documentation

Update all documentation and examples to the new version, commit, and push.

## 3. Tag

Push a tag with the new version.

```sh
git push HEAD:refs/tags/<major>.<minor>
```

This triggers a release in Github and Sonatype.
