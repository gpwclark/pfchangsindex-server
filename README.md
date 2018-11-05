# pfchangsindex-server

A Clojure library designed to serve graphql requests. It is currently in test
but serves as an endpoint to use for frontend development.

## lacinia bible

https://media.readthedocs.org/pdf/lacinia/latest/lacinia.pdf

## Usage

Currently still serving data from the lacinia-pedestal tutorial but will
update schema with actual schema for the pfchangs index.

To get the container up and running:
``
./build.sh
./run.sh
```

There is now a server running, go to http://localhost:8888/index.html.

Here you can interact with the data model.
1. Interact with the "Docs" section in the upper right-hand corner. This will 
explain the types of queries and the schema of each object returned.
2. The current queries defined are "pfchangs-by-id" and "list-pfchangs". In 
order to list all of the pfchangs and get only the name:
```
{
    pfchangs {
        name
    }
}
```
Since this is graphql we get only what we ask for. If we wanted all of the info
about all of the pfchangs we would write this query:
```
{
    pfchangs {
        description
        designers
        id
        max_players
        min_players
        name
        play_time
        summary
    }
}
```

We can also query for just one element, a 'pfchang'
```
{
    pfchang_by_id(id: "1235") {
        name
        description
        id
        max_players
        min_players
        summary
        play_time
        summary
    }
}
```

Each pfchang in this current data model has a "designer" property and we can
access its data:

```
{
    pfchang_by_id(id: "1235") {
        name
        description
        designers {
            id
            name
            pfchangs {
                id
            }
        }
        id
        max_players
        min_players
        summary
        play_time
        summary
    }
}
```

## License

Copyright © 2018 com.uofantarctica

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
