# SkyPub (🚧 WIP!!)

## Overview

A lightweight Bluesky Android client built by Compose Multiplatform.

## Tech stack & Open-source libraries

- Minimum SDK level 26
- Compose Multiplatform
- Support [MultiModule](https://developer.android.com/topic/modularization)
- Jetpack
    - Compose
    - ViewModel
    - Datastore
- Coroutines + Flow
- Koin: DI
- Voyager: Navigation
- Sketch: Image loading
- Material Design3
- kotlinx.serialization: JSON
- kotlinx.datetime: Date
- Napier: Logging
- Ktor: API
- Arrow: Error handling
- composite-build

## Architecture

The app architecture is based on
[Google's App Architecture Guide](https://developer.android.com/topic/architecture).
It consists of three major modules: app, core, and feature.

## Roadmap

- [ ] Cache for improving performance
- [ ] reply, repost
- [ ] user settings
- [ ] search
- [ ] multi-account
- [ ] support activitypub (If it's possible)
  and more...