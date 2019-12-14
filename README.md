# Dependency Cleaner

We all know how much **disk space** a languages dependencies folder can take. With this simple application, you can easily can an entire tree worth of a programming language dependencies.

Parses a (tree) of directories for dependency folders of different programming languages, deleting them provided there's a file with the list of dependencies, e.g., deletes a Python Virtual Environment directory only if there's a 'requirements.txt' file present.

Usage:
```bash
    java -jar dependencycleaner.jar -ddn {dependency_dir_name} -dfn {dependency_file_name} -i {dir_path} --deep
```