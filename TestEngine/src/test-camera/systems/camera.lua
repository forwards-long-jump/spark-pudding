function getRequiredComponents()
  return {entities = {"camera", "position", "size"},
    anchors = {"camera-anchor", "position", "size"},
    players = {"position", "size", "shoot-controller"}}
end

function update()
  for i, entity in ipairs(entities) do
    if entity.camera.enabled then
      game.camera:centerTargetAt(entity.position.x, entity.position.y, entity.size.width, entity.size.height)
      game.camera:setTargetScaling(entity.camera.scale)
    end
  end

  for i, entity in ipairs(anchors) do
    entity.camera.enabled = false
    for i, cEntity in ipairs(players) do
      cEntity.camera.enabled = true
      local y = cEntity.position.y
      local x = cEntity.position.x
      local w = cEntity.size.width
      local h = cEntity.size.height

      local x2 = entity.position.x
      local y2 = entity.position.y
      local w2 = entity.size.width
      local h2 = entity.size.height

      if x + w > x2 and y + h > y2 and x < x2 + w2 and y < y2 + h2 then
        entity.camera.enabled = true
        cEntity.camera.enabled = false

        game.camera:setSmoothScaleSpeedCoeff(0.05)
        game.camera:setSpringTranslateSpeedCoeff(entity["camera-anchor"].speed, entity["camera-anchor"].speed)
        entity.camera.scale = entity["camera-anchor"].scale
      end
    end
  end
end
