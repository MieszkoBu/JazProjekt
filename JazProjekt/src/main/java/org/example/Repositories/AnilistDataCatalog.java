package org.example.Repositories;

public class AnilistDataCatalog  implements ICatalogData{
    private final AnimeRepository animes;
    private final CompanyRepository companies;
    public AnilistDataCatalog(AnimeRepository animes, CompanyRepository companies){
        this.animes = animes;
        this.companies = companies;
    }

    public AnimeRepository getAnimes() {
        return animes;
    }

    public CompanyRepository getCompanies() {
        return companies;
    }

}
