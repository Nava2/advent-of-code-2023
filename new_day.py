#!/usr/bin/python3

import argparse
from pathlib import Path


def _touch(path: Path):
    '''
    Creates a file at the given path if it doesn't already exist.
    '''
    path.parent.mkdir(parents=True, exist_ok=True)
    path.touch(exist_ok=True)


def _rewrite_template(day_number: int, year: int, templated_input: Path, output: Path):
    output.parent.mkdir(parents=True, exist_ok=True)

    if output.exists():
        print(f'{output} already exists. Skipping.')
        return

    with output.open(mode='w') as f:
        with templated_input.open() as template:
            for templated_line in template:
                line = templated_line.replace('{{day_number}}', str(day_number)) \
                    .replace('{{year}}', str(year)) \
                    .replace('template', f'day{day_number}') \
                    .replace('Template', f'Day{day_number}')
                f.write(line)


def go(year: int, day_number: int):
    '''
    Reads from template files, replacing handlebars style variables with their equivalent values.
    '''

    # get the package path from net/navatwo/adventofcode{{year}}/day{{day_number}} replacing the variables
    templated_package_path_root = 'net/navatwo/adventofcode{{year}}'
    templated_package_path = templated_package_path_root + '/day{{day_number}}'
    package_path_root = templated_package_path_root.replace('{{year}}', str(year)).replace('{{day_number}}', str(day_number))
    package_path = templated_package_path.replace('{{year}}', str(year)).replace('{{day_number}}', str(day_number))

    p1_input = Path(f'src/main/resources/day{day_number}/p1_input.txt')
    _touch(p1_input)
    p1_sample = Path(f'src/test/resources/day{day_number}/p1_sample.txt')
    _touch(p1_sample)

    # rewrite the solution file
    templated_soln = Path(f'src/main/kotlin/{package_path_root}/template/TemplateSolution.kt')
    soln = Path(f'src/main/kotlin/{package_path}/Day{day_number}Solution.kt')

    _rewrite_template(day_number, year, templated_soln, soln)

    # rewrite the test file
    templated_test = Path(f'src/test/kotlin/{package_path_root}/template/TemplateSolutionTest.kt')
    test = Path(f'src/test/kotlin/{package_path}/Day{day_number}SolutionTest.kt')

    _rewrite_template(day_number, year, templated_test, test)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
        prog='generate day',
        description='Creates the files for a given day from templates.',
    )
    parser.add_argument('day_number', type=int)
    parser.add_argument('--year', default=2023, type=int)

    args = parser.parse_args()

    go(args.year, args.day_number)
