import os
import re
from pydantic import BaseModel
import json
import spotipy
from spotipy.oauth2 import SpotifyOAuth
import pprint


def song_search(name, artist):
    results = sp.search(artist, limit=5, type='artist')
    print(results)
    pprint.pprint(results)
    items = results['tracks']['items']
    if len(items) > 0:
        return items[0]['uri']
    else:
        return None


if __name__ == "__main__":
    client_id = '***REMOVED***'
    secret = '***REMOVED***'
    redirect_uri = 'https://127.0.0.1:8080/callback'
    sp = spotipy.Spotify(
        auth_manager=SpotifyOAuth(client_id=client_id, client_secret=secret, redirect_uri=redirect_uri)
    )   

    song_search("iu", "love wins all")
