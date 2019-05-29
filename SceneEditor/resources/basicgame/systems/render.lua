function getRequiredComponents()
  return {debugEntities = {"debug", "position", "size"}}
end

function renderStart()
  game.camera:applyTransforms(g:getContext())
  for i, entity in ipairs(debugEntities) do
    g:setColor(game.color:fromRGBA(entity.debug.r, entity.debug.g, entity.debug.b, entity.debug.a))
    g:fillRect(entity.position.x, entity.position.y, entity.size.width, entity.size.height)
  end
end

function renderEnd()
  game.camera:resetTransforms(g:getContext())
end