package org.example;

import org.example.GrahpQL.AniListGraphQL;
import org.example.GrahpQL.AniListGraphQLManga;
import org.example.GrahpQL.AniListGraphQLStudios;

public class AniListUpdater {
    public static void main(String[] args) {
        AniListGraphQLStudios.main(args);
        AniListGraphQL.main(args);
        AniListGraphQLManga.main(args);
    }
}