import spotipy
from spotipy.oauth2 import SpotifyClientCredentials
import pprint


def song_search(artist, song_name):
    client_id = '17969602a6a74820b44f5cacaad948ba'
    secret = 'a25f6e10ce724d8189c556401d4ba499'
    client_credentials_manager = SpotifyClientCredentials(client_id=client_id, client_secret=secret)
    sp = spotipy.Spotify(client_credentials_manager=client_credentials_manager)
    query = f'artist:{artist} track:{song_name}'
    print(f"Spotify search query: {query}")
    results = sp.search(q=query, type='track', limit=1)

    return results


if __name__ == "__main__":
    result_iu = song_search(artist="IU", song_name="Love wins all")
    pprint.pprint(result_iu)
