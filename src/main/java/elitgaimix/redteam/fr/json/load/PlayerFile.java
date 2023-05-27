package elitgaimix.redteam.fr.json.load;

import java.util.List;

import elitgaimix.redteam.fr.json.profile.Profile;

public class PlayerFile {
    private List<Profile> profile;

    public List<Profile> getProfile() {
        return this.profile;
    }

    public void setProfile(List<Profile> profile) {
        this.profile = profile;
    }

    public PlayerFile(List<Profile> profile) {
        this.profile = profile;
    }

}
