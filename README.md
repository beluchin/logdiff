# logdiff

* compares two text files.
* it can be configured to ignore certain differences which 
makes it useful when comparing snippets from two log files - hence 
the name `logdiff`
* differences are highlighted - a la `wdiff` 
```
$ logdiff <(echo a) <(echo b)
[-a-]{+b+}

$ wdiff <(echo a) <(echo b)
[-a-]{+b+}
```

<br>

* differences can be ignored by specifying rules<br>
`{ "hello" "hola" }` indicates to ignore the difference when `"hola"` 
is on the same place as `"hello"`
```
$ logdiff <(echo hello) <(echo hola) '{"hello" "hola"}'
== no output ==
```

<br>

* highlights ignored differences
```
$ logdiff <(echo hello a) <(echo hola b) '{"hello" "hola"}'
<hello><hola> [-a-]{+b+}
```

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
