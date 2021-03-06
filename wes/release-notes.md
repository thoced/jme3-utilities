# release notes for the Wes library and related tests

## Version 0.3.5 released on 14 February 2018

 + Remove an irrelevant test against track length from
   TrackEdit.setFrameTime() .

## Version 0.3.4 released on 7 February 2018

 + Fix a validation bug in TrackEdit.truncate() .
 + Add extractAnimation(), reverse(), and setFrameTime() methods to the
   TrackEdit class.

## Version 0.3.3 released on 2 February 2018

 + Add a simplify() method to the TrackEdit class.
 + Relax a validation constraint in TweenRotations.lerp() .

## Version 0.3.2 released on 25 January 2018

 + Base on heart library v2.0 to make this library physics-independent.

## Version 0.3.1 released on 22 January 2018

 + Add chain() and delayAll() methods to the TrackEdit class.
 + Target JME v3.2.1 .

## Version 0.3.0for32 released on 5 December 2017

 + 1st release to target JME v3.2
 + Utilize setTrackSpatial() with spatial tracks

## Version 0.2.4 released on 12 November 2017

 + Add an "endWeight" argument to the TrackEdit.wrap() method: an API change.
 + Handle null skeleton in Pose.rootBoneIndices().

## Version 0.2.3 released on 8 September 2017

 + Add fallback transforms to the interpolate() and transform() methods in
   TweenTransforms, for tracks that don't include all 3 transform components.
   These are API changes.

## Version 0.2.2 released on 7 September 2017

 + Generalize BoneTrack methods to also work for SpatialTracks. (This involved
   some API changes.)
 + Rename TweenTransforms.boneTransform() to transform().
 + Add newTrack() and setKeyframes() methods to TrackEdit class.

## Version 0.2.1 released on 4 September 2017

This was the initial baseline release.