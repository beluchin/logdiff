# logdiff

* compares two log snippets
* differences are higlighted - a la wdiff 
$ logdiff a b
[-a-]{+b+}

$ wdiff <(echo a) <(echo b)
[-a-]{+b+}

* some differences can be ignored by specifying rules
$ logdiff a b '{"a" "b"}
 == no output ==

## Installation

Download from http://example.com/FIXME.

## Usage

FIXME: explanation

    $ java -jar logdiff-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2019 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
